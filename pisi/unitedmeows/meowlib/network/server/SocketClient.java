package pisi.unitedmeows.meowlib.network.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;

import pisi.unitedmeows.meowlib.clazz.shared;

public class SocketClient {
	public static final shared<WTcpServer> sharedConnectedServer = new shared<>();
	protected SocketChannel socketChannel;
	private long lastHeartbeat;
	private final byte sharedIndex;
	private final Queue<byte[]> writeQueue;
	private DataInputStream dataInputStream;

	public SocketClient(final SocketChannel socketChannel, final byte sharedIndex) {
		this.socketChannel = socketChannel;
		this.sharedIndex = sharedIndex;
		try {
			dataInputStream = new DataInputStream(socketChannel.socket().getInputStream()); // FIXME 2 resource leaks
		} catch (final IOException e) {}
		writeQueue = new ArrayDeque<>();
	}

	public SocketChannel socketChannel() { return socketChannel; }

	public void beat() { lastHeartbeat = System.currentTimeMillis(); }

	public long lastHeartbeat() { return lastHeartbeat; }

	public void close() {
		try {
			socketChannel().close();
		} catch (final IOException e) {}
	}

	public Queue<byte[]> getWriteQueue() { return writeQueue; }

	public DataInputStream getDataInputStream() { return dataInputStream; }

	public void send(final byte[] data) { writeQueue.add(data); }

	public WTcpServer connectedServer() { return sharedConnectedServer.get(sharedIndex); }
}
