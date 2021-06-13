package pisi.unitedmeows.meowlib.network.server;

import pisi.unitedmeows.meowlib.clazz.shared;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static pisi.unitedmeows.meowlib.async.Async.async;

public class SocketClient {

    public static final shared<WTcpServer> sharedConnectedServer = new shared<>();

    protected SocketChannel socketChannel;
    private long lastHeartbeat;
    private final byte sharedIndex;

    public SocketClient(SocketChannel socketChannel, byte sharedIndex) {
        this.socketChannel = socketChannel;
        this.sharedIndex = sharedIndex;
    }

    public SocketChannel socketChannel() {
        return socketChannel;
    }

    public void beat() {
        lastHeartbeat = System.currentTimeMillis();
    }

    public long lastHeartbeat() {
        return lastHeartbeat;
    }

    public void close() {
        try {
            socketChannel().close();
        } catch (IOException e) {

        }
    }

    public void send(byte[] data) {
        async(uuid -> {
            try {
                socketChannel.write(ByteBuffer.wrap(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public WTcpServer connectedServer() {
        return sharedConnectedServer.get(sharedIndex);
    }

}
