package pisi.unitedmeows.meowlib.signal;

import static pisi.unitedmeows.meowlib.async.Async.async;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import pisi.unitedmeows.meowlib.etc.CoID;
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.client.WTcpClient;
import pisi.unitedmeows.meowlib.network.client.events.DCDataReceived;
import pisi.unitedmeows.meowlib.thread.kThread;
import stelix.xfile.SxfDataBlock;
import stelix.xfile.reader.SxfReader;

public class Signal {
	protected static final byte[] GET_INFO = ";SIGNAL->GET_INFO".getBytes();
	/* TODO: USE STELIX TO SEND DATA */
	/* SXF 2.0 */
	/* 2950 - 3100 */

	private Signal() { throw new IllegalStateException("Utility class"); }

	public static List<SignalApp> discover(final IPAddress address) {
		final List<Integer> ports = discover_ports(address);
		final List<SignalApp> apps = new ArrayList<>();
		final SxfReader reader = new SxfReader();
		for (final int port : ports) {
			final WTcpClient client = new WTcpClient();
			client.connect(address, port);
			if (client.isConnected()) {
				client.send(Signal.GET_INFO);
				client.dataReceivedEvent.bind(new DCDataReceived() {
					@Override
					public void onDataReceived(final byte[] data) {
						final SxfDataBlock appInfo = reader.readRaw(new String(data)).dataBlock("app");
						final SignalApp signalApp = new SignalApp(appInfo.variable("appName"), appInfo.variable("appVersion"), new CoID(appInfo.variable("appKey")));
						apps.add(signalApp);
					}
				});
			}
			kThread.sleep(1000);
			client.close();
		}
		return apps;
	}

	public static List<Integer> discover_ports(final IPAddress address /* why isnt address used */) {
		final List<Integer> ports = new ArrayList<>();
		for (int port = 2950; port < 3100; port++) if (available(port)) ports.add(port);
		return ports;
	}

	/**
	 * @deprecated add a reason sometime..
	 */
	@Deprecated
	public static List<Integer> discover_ports_fast(final IPAddress address) {
		final List<Integer> ports = new ArrayList<>();
		for (int port = 2950; port < 3100; port++) { final int finalPort = port; async(u -> { if (available(finalPort)) ports.add(finalPort); }); }
		kThread.sleep(1000);
		return ports;
	}

	private static boolean available(final int port) {
		try (Socket socket = new Socket() /* closing the resource is handled if there is a try catch and the try catch block is like this */ ) {
			socket.connect(new InetSocketAddress("localhost", port));
			/* socket.close(); <- not needed */
			return socket.isConnected();
		} catch (final Exception ex) {
			return false;
		}
	}
}
