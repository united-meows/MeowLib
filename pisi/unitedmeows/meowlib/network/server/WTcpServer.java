package pisi.unitedmeows.meowlib.network.server;

import static pisi.unitedmeows.meowlib.async.Async.*;

import com.sun.security.ntlm.Server;
import pisi.unitedmeows.meowlib.async.Promise;
import pisi.unitedmeows.meowlib.clazz.event;
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.thread.kThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class WServer {

    private IPAddress bindAddress;

    public event<DConnectionRequest> connectionRequest = new event<>();
    public event<DClientDataReceived> dataReceived = new event<>();

    private ServerSocketChannel serverSocket;

    private List<SocketChannel> connectedClients;

    private Thread readingThread;

    private int port;

    private Promise connectionListenerPromise;

    public WServer(IPAddress ipAddress, int port) {
        bindAddress = ipAddress;
        this.port = port;
        connectedClients = new ArrayList<>();
    }

    public IPAddress bindAddress() {
        return bindAddress;
    }

    public WServer start() {
        try {
            serverSocket = ServerSocketChannel.open();
            serverSocket.configureBlocking(false);
            serverSocket.bind(new InetSocketAddress(bindAddress().getAddress(), port));
            readingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ByteBuffer buffer = ByteBuffer.allocate(2048 * 10);
                    while (serverSocket.isOpen()) {
                        for (SocketChannel client : connectedClients) {
                            buffer.clear();
                            try {
                                int read = client.read(buffer);
                                if (read > 0) {
                                    buffer.flip();
                                    dataReceived.run(client, buffer);
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

    public WServer listen() {
        start();

        connectionListenerPromise = async_loop(u -> {
            try {
                // Client connecting to the server
                SocketChannel socketChannel = serverSocket.accept();
                if (socketChannel != null) {

                    socketChannel.configureBlocking(false);
                    connectionRequest.run(socketChannel);
                    if (socketChannel.isConnected()) {
                        connectedClients.add(socketChannel);

                    }
                }

            } catch (IOException e) {

            }
        }, 20);




        /* and listen for connections */
        return this;
    }

    public void send(SocketChannel socket, byte[] data) {
        try {
            socket.write(ByteBuffer.wrap(data));
        } catch (IOException e) {

        }
    }

    public WServer kick(SocketChannel socketChannel) {
        try {
            socketChannel.close();
        } catch (IOException e) {

        }
        return this;
    }
}
