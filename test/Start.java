package test;

import pisi.unitedmeows.meowlib.clazz.updatable;
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.client.WTcpClient;
import pisi.unitedmeows.meowlib.network.server.SocketClient;
import pisi.unitedmeows.meowlib.network.server.WTcpServer;
import pisi.unitedmeows.meowlib.network.server.events.DSDataReceived;
import pisi.unitedmeows.meowlib.thread.kThread;

public class Start {
	private static final updatable<String> variable = new updatable<String>("", 200) {
		@Override
		public void update() { value += "e"; }
	};

	// i hate java <- this guy is cringe
	public static void main(final String[] args) {
		final WTcpServer wTcpServer = new WTcpServer(IPAddress.LOOPBACK, 2173);
		wTcpServer.listen();
		wTcpServer.dataReceivedEvent.bind(new DSDataReceived() {
			@Override
			public void onDataReceived(SocketClient client, byte[] data) {
				System.out.println(new String(data));
			}
		});
		kThread.sleep(1000);
		final WTcpClient client = new WTcpClient();
		client.connect(IPAddress.LOOPBACK, 2173);
		int i = 0;
		while (true) { client.send(("1LOEZEZEZL EZ EZ EZ EZ LOL eEZEZEZ z ez ez ez ez EeZEZEZEZE ZEZ ZEZEZ " + i).getBytes()); kThread.sleep(1); i++; }
	}
}
