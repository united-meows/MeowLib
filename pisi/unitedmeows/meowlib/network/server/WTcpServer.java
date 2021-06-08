package pisi.unitedmeows.meowlib.network.server;

import static pisi.unitedmeows.meowlib.async.Async.*;

import pisi.unitedmeows.meowlib.async.Promise;
import pisi.unitedmeows.meowlib.clazz.event;
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.NetworkConstants;
import pisi.unitedmeows.meowlib.network.server.events.DSClientQuit;
import pisi.unitedmeows.meowlib.network.server.events.DSDataReceived;
import pisi.unitedmeows.meowlib.network.server.events.DSConnectionRequest;
import pisi.unitedmeows.meowlib.thread.kThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class WTcpServer {

    private IPAddress bindAddress;

    public event<DSConnectionRequest> connectionRequestEvent = new event<>();
    public event<DSDataReceived> dataReceivedEvent = new event<>();
    public event<DSClientQuit> clientQuitEvent = new event<>();

    private ServerSocketChannel serverSocket;

    private HashMap<SocketChannel, Long> connectedClients;

    private Thread readingThread;

    private int port;

    private boolean keepAlive;
    private long maxKeepAliveInterval;
    private Promise keepAlivePromise;

    private Promise connectionListenerPromise;

    public WTcpServer(IPAddress ipAddress, int port) {
        bindAddress = ipAddress;
        this.port = port;
        connectedClients = new HashMap<>();
    }

    public IPAddress bindAddress() {
        return bindAddress;
    }

    public WTcpServer start() {
        try {
            serverSocket = ServerSocketChannel.open();
            serverSocket.configureBlocking(false);
            serverSocket.bind(new InetSocketAddress(bindAddress().getAddress(), port));
            readingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ByteBuffer buffer = ByteBuffer.allocate(2048 * 10);
                    while (serverSocket.isOpen()) {
                        for (SocketChannel client : connectedClients.keySet()) {
                            buffer.clear();
                            try {
                                int read = client.read(buffer);
                                byte[] data = Arrays.copyOfRange(buffer.array(), 0, read);
                                if (read > 0) {
                                    buffer.flip();
                                    if (Arrays.equals(NetworkConstants.KEEPALIVE_DATA, data)) {
                                        connectedClients.put(client, System.currentTimeMillis());

                                        buffer.clear();
                                        continue;
                                    }

                                    dataReceivedEvent.run(client, buffer);
                                    buffer.clear();

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        kThread.sleep(1);
                    }
                }
            });
        } catch (Exception ex) {
            return null;
        }
        readingThread.start();
        return this;
    }

    public void stop() {
        connectionListenerPromise.stop();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WTcpServer listen() {
        start();
        if (connectionListenerPromise != null)
            connectionListenerPromise.stop();

        connectionListenerPromise = async_loop(u -> {
            try {
                // Client connecting to the server
                SocketChannel socketChannel = serverSocket.accept();
                if (socketChannel != null) {

                    socketChannel.configureBlocking(false);
                    connectionRequestEvent.run(socketChannel);
                    if (socketChannel.isConnected()) {
                        connectedClients.put(socketChannel, System.currentTimeMillis());

                    }
                }

            } catch (IOException e) {

            }
        }, 20);
        if (keepAlivePromise != null)
            keepAlivePromise.stop();

        if (keepAlive) {
            keepAlivePromise = async_loop(u -> {
                List<SocketChannel> kickedList = new ArrayList<>();
                for (Map.Entry<SocketChannel, Long> channel : connectedClients.entrySet()) {
                    if (System.currentTimeMillis() - channel.getValue() >= maxKeepAliveInterval) {
                        kickedList.add(channel.getKey());
                    }
                }
                // kick expired keepalive timers
                kickedList.forEach(x -> {
                    kick(x);
                    clientQuitEvent.run(x);
                });

            }, 1000);
        }




        /* and listen for connections */
        return this;
    }

    public void send(SocketChannel socket, byte[] data) {
        try {
            socket.write(ByteBuffer.wrap(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WTcpServer kick(SocketChannel socketChannel) {
        try {
            connectedClients.remove(socketChannel);
            socketChannel.close();
        } catch (IOException e) {

        }
        return this;
    }

    public WTcpServer setMaxKeepAliveInterval(long maxKeepAliveInterval) {
        this.maxKeepAliveInterval = maxKeepAliveInterval;
        return this;
    }

    public WTcpServer setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }
}
