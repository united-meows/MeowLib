package pisi.unitedmeows.meowlib.network.server;

import pisi.unitedmeows.meowlib.async.Async;
import pisi.unitedmeows.meowlib.clazz.event;
import pisi.unitedmeows.meowlib.network.client.events.DCDataReceived;
import pisi.unitedmeows.meowlib.network.server.events.DSDataReceived;
import pisi.unitedmeows.meowlib.network.server.events.DTCDataSendRequest;
import pisi.unitedmeows.meowlib.network.server.events.DTSDataSendRequest;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WCTcpTunnel {

    public event<DTCDataSendRequest> dataSendRequestEvent = new event<>();

    private SocketChannel client1, client2;

    private WTcpServer server;

    private int receiveEventId;

    public WCTcpTunnel(WTcpServer server, SocketClient client1, SocketClient client2) {
        this(server, client1.socketChannel(), client2.socketChannel());
    }

    public WCTcpTunnel(WTcpServer server, SocketChannel client1, SocketChannel client2) {
        if (!client1.isConnected() || !client2.isConnected()) {
            return;
        }

        this.client1 = client1;
        this.client2 = client2;
        this.server = server;

        receiveEventId = server.dataReceivedEvent.bind(new DSDataReceived() {
            @Override
            public void onDataReceived(SocketClient client, byte[] data) {
                if (client.socketChannel() == client1) {
                    DTCDataSendRequest.Args args = new DTCDataSendRequest.Args();
                    dataSendRequestEvent.run();
                    if (!args.canceled) {
                        server.send(client2, data);
                    }
                } else if (client.socketChannel() == client2) {
                    DTCDataSendRequest.Args args = new DTCDataSendRequest.Args();
                    dataSendRequestEvent.run();
                    if (!args.canceled) {
                        server.send(client1, data);;
                    }
                }
            }
        });
    }

    public SocketChannel client1() {
        return client1;
    }

    public SocketChannel client2() {
        return client2;
    }

    public void closeTunnel() {
        server.dataReceivedEvent.unbind(receiveEventId);
    }

    public void closeTunnelAndConnections() {
        closeTunnel();
        server.stop();
    }
}
