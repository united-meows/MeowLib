package pisi.unitedmeows.meowlib.network.server;

import pisi.unitedmeows.meowlib.async.Promise;
import pisi.unitedmeows.meowlib.clazz.shared;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;

import static pisi.unitedmeows.meowlib.async.Async.async;
import static pisi.unitedmeows.meowlib.async.Async.async_loop;

public class SocketClient {

    public static final shared<WTcpServer> sharedConnectedServer = new shared<>();

    protected SocketChannel socketChannel;
    private long lastHeartbeat;
    private final byte sharedIndex;

    private Queue<byte[]> writeQueue;
    private final Promise writePromise;

    private DataInputStream dataInputStream;

    public SocketClient(SocketChannel socketChannel, byte sharedIndex) {
        this.socketChannel = socketChannel;
        this.sharedIndex = sharedIndex;

        try {
            dataInputStream = new DataInputStream(socketChannel.socket().getInputStream());
        } catch (IOException e) {

        }
        writeQueue = new ArrayDeque<>();

        writePromise = async_loop(u -> {
            if (!writeQueue.isEmpty()) {
                byte[] data = writeQueue.poll();
                try {
                    socketChannel.write(ByteBuffer.wrap(data));
                } catch (IOException e) {

                }
            }
        }, 1);
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
        if (writePromise != null) {
            writePromise.stop();
        }

        try {
            socketChannel().close();
        } catch (IOException e) {

        }
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public void send(byte[] data) {
        writeQueue.add(data);
    }

    public WTcpServer connectedServer() {
        return sharedConnectedServer.get(sharedIndex);
    }

}
