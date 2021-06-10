package pisi.unitedmeows.meowlib.network.server;

import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.client.WTcpClient;
import pisi.unitedmeows.meowlib.network.server.events.DSDataReceived;

import java.nio.channels.SocketChannel;

public class WTcpClientPool {

    protected WTcpServer server;

    protected int dataReceiveKey;
    /* todo: add shared<WTcpServer> here */

    public WTcpClientPool(IPAddress address, int port) {
        this(new WTcpServer(address, port));
    }


    public WTcpClientPool(WTcpServer server) {
        this.server = server;
        _start();
    }

    private void _start() {
        dataReceiveKey = server().dataReceivedEvent.bind(new DSDataReceived() {
            @Override
            public void onDataReceived(SocketClient client, byte[] data) {
                for (SocketClient connectedClient : server.connectedClients()) {
                    connectedClient.send(data);
                }
            }
        });
    }

    public void closePool() {
        server().dataReceivedEvent.unbind(dataReceiveKey);
    }

    public void closePoolAndServer() {
        closePool();
        server().stop();
    }

    public WTcpServer server() {
        return server;
    }

}
