package pisi.unitedmeows.meowlib.network.server;

import static pisi.unitedmeows.meowlib.async.Async.*;

import pisi.unitedmeows.meowlib.async.Promise;
import pisi.unitedmeows.meowlib.clazz.event;
import pisi.unitedmeows.meowlib.ex.Ex;
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.NetworkConstants;
import pisi.unitedmeows.meowlib.network.client.WTcpClient;
import pisi.unitedmeows.meowlib.network.server.events.DSClientQuit;
import pisi.unitedmeows.meowlib.network.server.events.DSDataReceived;
import pisi.unitedmeows.meowlib.network.server.events.DSConnectionRequest;
import pisi.unitedmeows.meowlib.thread.kThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.net.SocketOptions;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class WTcpServer {

    private IPAddress bindAddress;
    private byte serverId;
    public event<DSConnectionRequest> connectionRequestEvent = new event<>();
    public event<DSDataReceived> dataReceivedEvent = new event<>();
    public event<DSClientQuit> clientQuitEvent = new event<>();

    private ServerSocketChannel serverSocket;

    private CopyOnWriteArrayList<SocketClient> connectedClients;

    private Thread readingThread;
    private Thread writeThread;
    private Thread connectionThread;

    private int port;

    private boolean keepAlive;
    private long maxKeepAliveInterval;
    private Promise keepAlivePromise;


    public WTcpServer(IPAddress ipAddress, int port) {
        bindAddress = ipAddress;
        this.port = port;
        connectedClients = new CopyOnWriteArrayList<>();
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
                            try {
                                int read = client.socketChannel().read(buffer);
                                if (read == -1) {
                                    kick(client);
                                    continue;
                                }

                                byte[] data = Arrays.copyOfRange(buffer.array(), 0, read);
                                buffer.clear();
                                if (read > 0) {
                                    if (Arrays.equals(NetworkConstants.KEEPALIVE_DATA, data)) {
                                        client.beat();
                                        continue;
                                    }

                                    dataReceivedEvent.run(client, data);


                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        kThread.sleep(1L);
                    }
                }
            });
            if (writeThread != null) {
                try {
                    writeThread.stop();
                } catch (Exception ex) {}
            }
            writeThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (serverSocket.isOpen()) {
                        for (SocketClient connectedClient : connectedClients) {
                            if (!connectedClient.getWriteQueue().isEmpty()) {
                                byte[] sendData = connectedClient.getWriteQueue().poll();
                                try {
                                    int result = connectedClient.socketChannel().write(ByteBuffer.wrap(sendData));
                                    if (result == -1) {
                                        kick(connectedClient);
                                    }
                                } catch (IOException e) {

                                }
                            }
                        }
                        kThread.sleep(10);
                    }
                }
            });
            writeThread.start();
            serverId = SocketClient.sharedConnectedServer.put(this);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        if (connectionThread != null) {
            try {
                connectionThread.stop();
            } catch (Exception ex) {

            }
        }

        connectionThread = new Thread(this::connectionListener);
        connectionThread.start();
        readingThread.start();
        return this;
    }


    private void connectionListener() {
        while (serverSocket.isOpen()) {
            try {
                // Client connecting to the server
                SocketChannel socketChannel = serverSocket.accept();

                if (socketChannel != null) {
                    socketChannel.socket().setTcpNoDelay(true);
                    socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
                    socketChannel.configureBlocking(false);
                    SocketClient socketClient = new SocketClient(socketChannel, serverId);
                    socketClient.beat();
                    connectedClients.add(socketClient);
                    connectionRequestEvent.run(socketClient);
                }

            } catch (IOException e) {

            }
            kThread.sleep(20);
        }
    }

    public void stop() {
        SocketClient.sharedConnectedServer.remove(serverId);
        try {
            try {
                connectionThread.stop();
            } catch (Exception ex) {}
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WTcpServer listen() {
        start();

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
