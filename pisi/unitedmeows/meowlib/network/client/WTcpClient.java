package pisi.unitedmeows.meowlib.network.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

import pisi.unitedmeows.meowlib.async.Async;
import pisi.unitedmeows.meowlib.async.Promise;
import pisi.unitedmeows.meowlib.clazz.event;
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.NetworkConstants;
import pisi.unitedmeows.meowlib.network.client.events.DCDataReceived;
import pisi.unitedmeows.meowlib.thread.kThread;

@SuppressWarnings("resource") // no resource leaks, we can ignore
public class WTcpClient {
	private final Socket socket;
	public static final event<DCDataReceived> dataReceivedEvent = new event<>();
	private final byte[] BUFFER = new byte[4096 * 2];
	private Thread receiveThread;
	private Thread writeThread;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private final Queue<byte[]> writeQueue;
	private boolean keepAlive;
	private int keepAliveInterval = 20000;
	private Promise keepAlivePromise;
	private int index;

	public WTcpClient(/* proxy support ? */) {
		socket = new Socket();
		writeQueue = new ArrayDeque<>();
		index = 0;
		// try {
		// socket.setTcpNoDelay(true);
		// } catch (final IOException e) {
		// e.printStackTrace();
		// }
	}

	public WTcpClient connect(final IPAddress ipAddress, final int port) {
		try {
			final InetAddress inetAddress = InetAddress.getByName(ipAddress.getAddress());
			final SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
			socket.connect(socketAddress);
			// socket.setTcpNoDelay(true);
		} catch (final IOException e) {
			/* find a better way for exceptions **/
			e.printStackTrace(); // print the stacktrace for now...
		}
		try {
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		if (receiveThread != null) try {
			receiveThread.stop();
		} catch (final Exception ex) {
			// todo:
		}
		if (keepAlivePromise != null) keepAlivePromise.stop();
		if (keepAlive) keepAlivePromise = Async.async_loop(x -> {
			// send keepalive
			if (socket.isConnected()) send(NetworkConstants.KEEPALIVE_DATA);
		}, keepAliveInterval);
		if (writeThread != null) writeThread.stop();
		writeThread = new Thread(() -> {
			try {
				Thread.sleep(250);
			} catch (final InterruptedException e) {
				e.printStackTrace();
				writeThread.interrupt(); // certainly should be interrupted
			}
			while (socket.isConnected()) if (!writeQueue.isEmpty()) {
				final byte[] current = writeQueue.poll();
				_send(current);
				index++;
				kThread.sleep(4L);
			}
		});
		receiveThread = new Thread(this::receive);
		receiveThread.start();
		writeThread.start();
		return this;
	}

	private synchronized void _send(final byte[] data) {
		try {
			outputStream.write(data, 0, data.length);
			outputStream.flush();
		} catch (final Exception ex) {}
	}

	public void send(final byte[] data) { writeQueue.add(data); }

	public boolean isConnected() { return socket.isConnected(); }

	private void receive() {
		while (!socket.isClosed()) try {
			final int size = inputStream.read(BUFFER);
			final byte[] data = Arrays.copyOf(BUFFER, size);
			// received
			dataReceivedEvent.run(data);
		} catch (final Exception ignore) {}
	}

	public void close() {
		dataReceivedEvent.unbindAll();
		if (keepAlivePromise != null) keepAlivePromise.stop();
		try {
			if (writeThread != null) writeThread.stop();
		} catch (final Exception ex) {} // personally think exceptions should not be ignored
		try {
			if (receiveThread != null) receiveThread.stop();
		} catch (final Exception ex) {}
		writeQueue.clear();
		try {
			socket.close();
		} catch (final IOException e) {
			e.printStackTrace(); // close exceptions should not be ignored
		}
	}

	public boolean isKeepAlive() { return keepAlive; }

	public WTcpClient setKeepAliveInterval(final int keepAliveInterval) {
		this.keepAliveInterval = keepAliveInterval;
		return this;
	}

	public WTcpClient setKeepAlive(final boolean keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}

	public Socket socket() { return socket; }
}
