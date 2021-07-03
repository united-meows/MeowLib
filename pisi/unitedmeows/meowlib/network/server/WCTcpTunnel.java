package pisi.unitedmeows.meowlib.network.server;

import java.nio.channels.SocketChannel;

import pisi.unitedmeows.meowlib.clazz.event;
import pisi.unitedmeows.meowlib.network.server.events.DSDataReceived;
import pisi.unitedmeows.meowlib.network.server.events.DTCDataSendRequest;

public class WCTcpTunnel {
	public static final /* no need for acessors */ event<DTCDataSendRequest> dataSendRequestEvent = new event<>();
	private SocketClient client1 , client2;
	private WTcpServer server;
	private int receiveEventId;
	private byte serverId;

	public WCTcpTunnel(final WTcpServer server, final SocketClient client1, final SocketClient client2) {
		this(server, client1.socketChannel(), client2.socketChannel()); // FIXME when does client1 and client2 close?
	}

	public WCTcpTunnel(final WTcpServer server, final SocketChannel client1, final SocketChannel client2) {
		if (!client1.isConnected() || !client2.isConnected()) return;
		this.server = server;
		this.serverId = SocketClient.sharedConnectedServer.put(server);
		this.client1 = new SocketClient(client1, serverId);
		this.client2 = new SocketClient(client2, serverId);
		receiveEventId = server.dataReceivedEvent.bind(new DSDataReceived() {
			@Override
			public void onDataReceived(final SocketClient client, final byte[] data) {
				if (client.socketChannel() == client1) { // FIXME resource leak
					final DTCDataSendRequest.Args args = new DTCDataSendRequest.Args();
					dataSendRequestEvent.run();
					if (!args.canceled) client2().send(data);
				} else if (client.socketChannel() == client2) { // FIXME resource leak
					final DTCDataSendRequest.Args args = new DTCDataSendRequest.Args();
					dataSendRequestEvent.run();
					if (!args.canceled) client1().send(data);
				}
			}
		});
	}

	public SocketClient client1() { return client1; }

	public SocketClient client2() { return client2; }

	public void closeTunnel() { server.dataReceivedEvent.unbind(receiveEventId); }

	public void closeTunnelAndConnections() {
		closeTunnel();
		server.stop();
	}
}
