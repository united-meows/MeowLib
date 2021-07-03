package pisi.unitedmeows.meowlib.network.server;

import static pisi.unitedmeows.meowlib.async.Async.async_loop;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jdk.net.ExtendedSocketOptions;
import pisi.unitedmeows.meowlib.async.Promise;
import pisi.unitedmeows.meowlib.clazz.event;
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.NetworkConstants;
import pisi.unitedmeows.meowlib.network.server.events.DSClientQuit;
import pisi.unitedmeows.meowlib.network.server.events.DSConnectionRequest;
import pisi.unitedmeows.meowlib.network.server.events.DSDataReceived;
import pisi.unitedmeows.meowlib.thread.kThread;

/**
 * FIXME: TOO MANY RESOURCE LEAKS BROOOOO WTF
 */
public class WTcpServer {
	private final IPAddress bindAddress;
	private byte serverId;
	public event<DSConnectionRequest> connectionRequestEvent = new event<>();
	public event<DSDataReceived> dataReceivedEvent = new event<>();
	public event<DSClientQuit> clientQuitEvent = new event<>();
	private ServerSocketChannel serverSocket;
	private final CopyOnWriteArrayList<SocketClient> connectedClients;
	private Thread readingThread;
	private Thread writeThread;
	private Thread connectionThread;
	private final int port;
	private boolean keepAlive;
	private long maxKeepAliveInterval;
	private Promise keepAlivePromise;

	public WTcpServer(final IPAddress ipAddress, final int port) {
		bindAddress = ipAddress;
		this.port = port;
		connectedClients = new CopyOnWriteArrayList<>();
	}

	public IPAddress bindAddress() { return bindAddress; }

	public WTcpServer start() {
		try {
			serverSocket = ServerSocketChannel.open();
			serverSocket.setOption(ExtendedSocketOptions.TCP_QUICKACK, true);
			serverSocket.bind(new InetSocketAddress(bindAddress().getAddress(), port));
			readingThread = new Thread(() -> {
				final ByteBuffer buffer = ByteBuffer.allocate(2048 * 10);
				while (serverSocket.isOpen()) {
					for (final SocketClient client : connectedClients) try {
						final int read = client.socketChannel().read(buffer);
						if (read == -1) {
							kick(client);
							continue;
						}
						if (read > 0) {
							final byte[] data = Arrays.copyOfRange(buffer.array(), 0, read);
							buffer.clear();
							if (Arrays.equals(NetworkConstants.KEEPALIVE_DATA, data)) {
								client.beat();
								continue;
							}
							dataReceivedEvent.run(client, data);
						}
					} catch (final IOException e) {
						e.printStackTrace();
					}
					kThread.sleep(1L);
				}
			});
			if (writeThread != null) try {
				writeThread.stop();
			} catch (final Exception ex) {}
			writeThread = new Thread(() -> {
				while (serverSocket.isOpen()) {
					for (final SocketClient connectedClient : connectedClients) if (!connectedClient.getWriteQueue().isEmpty()) {
						final byte[] sendData = connectedClient.getWriteQueue().poll();
						try {
							final int result = connectedClient.socketChannel().write(ByteBuffer.wrap(sendData));
							if (result == -1) kick(connectedClient);
						} catch (final IOException e) {}
					}
					kThread.sleep(10);
				}
			});
			writeThread.start();
			serverId = SocketClient.sharedConnectedServer.put(this);
		} catch (final Exception ex) {
			ex.printStackTrace();
			return null;
		}
		if (connectionThread != null) try {
			connectionThread.stop();
		} catch (final Exception ex) {}
		connectionThread = new Thread(this::connectionListener);
		connectionThread.start();
		readingThread.start();
		return this;
	}

	private void connectionListener() {
		while (serverSocket.isOpen()) {
			try {
				// Client connecting to the server
				final SocketChannel socketChannel = serverSocket.accept();
				if (socketChannel != null) {
					final SocketClient socketClient = new SocketClient(socketChannel, serverId);
					socketClient.beat();
					connectedClients.add(socketClient);
					connectionRequestEvent.run(socketClient);
				}
			} catch (final IOException e) {}
			kThread.sleep(20);
		}
	}

	public void stop() {
		SocketClient.sharedConnectedServer.remove(serverId);
		try {
			try {
				connectionThread.stop();
			} catch (final Exception ex) {}
			serverSocket.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public WTcpServer listen() {
		start();
		if (keepAlivePromise != null) keepAlivePromise.stop();
		if (keepAlive) keepAlivePromise = async_loop(u -> {
			final List<SocketClient> kickedList = new ArrayList<>();
			final long currentTime = System.currentTimeMillis();
			for (final SocketClient client : connectedClients) if (currentTime - client.lastHeartbeat() >= maxKeepAliveInterval) kickedList.add(client);
			// kick expired keepalive timers
			kickedList.forEach(x -> {
				kick(x);
				clientQuitEvent.run(x);
			});
		}, 1000);
		/* and listen for connections */
		return this;
	}

	public WTcpServer kick(final SocketClient socketClient) {
		connectedClients.remove(socketClient);
		socketClient.close();
		return this;
	}

	public List<SocketClient> connectedClients() { return connectedClients; }

	public WTcpServer setMaxKeepAliveInterval(final long maxKeepAliveInterval) {
		this.maxKeepAliveInterval = maxKeepAliveInterval;
		return this;
	}

	public WTcpServer setKeepAlive(final boolean keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}
}
