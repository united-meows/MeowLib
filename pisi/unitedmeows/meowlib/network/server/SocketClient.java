package pisi.unitedmeows.meowlib.network.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static pisi.unitedmeows.meowlib.async.Async.async;

public class SocketClient {

    protected SocketChannel socketChannel;
    private long lastHeartbeat;

    public SocketClient(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
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

}
