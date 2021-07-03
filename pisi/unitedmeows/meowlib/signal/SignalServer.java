package pisi.unitedmeows.meowlib.signal;

import java.util.Arrays;

import pisi.unitedmeows.meowlib.etc.CoID;
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.server.SocketClient;
import pisi.unitedmeows.meowlib.network.server.WTcpServer;
import pisi.unitedmeows.meowlib.network.server.events.DSDataReceived;
import pisi.unitedmeows.meowlib.random.WRandom;
import stelix.xfile.SxfDataBlock;
import stelix.xfile.SxfFile;
import stelix.xfile.writer.SxfWriter;

public class SignalServer {
	private final WTcpServer tcpServer;

	public SignalServer(final String appName, final double appVersion, final CoID appKey) {
		final SxfFile appFile = new SxfFile();
		final SxfDataBlock app = new SxfDataBlock();
		app.putVar("appName", appName);
		app.putVar("appVersion", appVersion);
		app.putVar("appKey", appKey.toString());
		appFile.put("app", app);
		final SxfWriter writer = new SxfWriter();
		writer.setWriteType(SxfWriter.WriteType.INLINE);
		final byte[] appData = writer.writeToString(appFile).getBytes();
		tcpServer = new WTcpServer(IPAddress.LOOPBACK, WRandom.BASIC.nextInRange(2950, 3100));
		tcpServer.dataReceivedEvent.bind(new DSDataReceived() {
			@Override
			public void onDataReceived(final SocketClient client, final byte[] data) {
				if (Arrays.equals(data, Signal.GET_INFO)) {
					client.send(appData);
					System.out.println(new String(appData)); // LOGGER WHEN??
				}
			}
		});
	}

	public void listen() { tcpServer.listen(); }
}
