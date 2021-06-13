package pisi.unitedmeows.meowlib.network.server;

import static pisi.unitedmeows.meowlib.async.Async.*;

import pisi.unitedmeows.meowlib.async.Promise;
import pisi.unitedmeows.meowlib.clazz.event;
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.NetworkConstants;
import pisi.unitedmeows.meowlib.network.client.WTcpClient;
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
    private byte serverId;
    public event<DSConnectionRequest> connectionRequestEvent = new event<>();
    public event<DSDataReceived> dataReceivedEvent = new event<>();
    public event<DSClientQuit> clientQuitEvent = new event<>();

    private ServerSocketChannel serverSocket;

    private List<SocketClient> connectedClients;

    private Thread readingThread;

    private int port;

    private boolean keepAlive;
    private long maxKeepAliveInterval;
    private Promise keepAlivePromise;

    private Promise connectionListenerPromise;

    public WTcpServer(IPAddress ipAddress, int port) {
        bindAddress = ipAddress;
        this.port = port;
        connectedClients = new ArrayList<>();
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
                        for (SocketClient client : connectedClients) {
                            buffer.clear();
                            try {
                                int read = client.socketChannel().read(buffer);
                                byte[] data = Arrays.copyOfRange(buffer.array(), 0, read);
                                if (read > 0) {
                                    buffer.flip();
                                    if (Arrays.equals(NetworkConstants.KEEPALIVE_DATA, data)) {
                                        client.beat();
                                        buffer.clear();
                                        continue;
                                    }

                                    dataReceivedEvent.run(client, data);
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
            serverId = SocketClient.sharedConnectedServer.put(this);
        } catch (Exception ex) {
            return null;
        }

        readingThread.start();
        return this;
    }

    public void stop() {
        connectionListenerPromise.stop();
        SocketClient.sharedConnectedServer.remove(serverId);
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
                        SocketClient socketClient = new SocketClient(socketChannel, serverId);
                        socketClient.beat();
                        connectedClients.add(socketClient);


                    }
                }

            } catch (IOException e) {

            }
        }, 20);
        if (keepAlivePromise != null)
            keepAlivePromise.stop();

        if (keepAlive) {
            keepAlivePromise = async_loop(u -> {
                List<SocketClient> kickedList = new ArrayList<>();
                final long currentTime = System.currentTimeMillis();
                for (SocketClient client : connectedClients) {
                    if (currentTime - client.lastHeartbeat() >= maxKeepAliveInterval) {
                        kickedList.add(client);
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
        async(uuid -> {
            try {
                socket.write(ByteBuffer.wrap(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public WTcpServer kick(SocketClient socketClient) {
        connectedClients.remove(socketClient);
        socketClient.close();
        return this;
    }

    public List<SocketClient> connectedClients() {
        return connectedClients;
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
