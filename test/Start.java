package test;

import static pisi.unitedmeows.meowlib.async.Async.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import pisi.unitedmeows.meowlib.MeowLib;
import static pisi.unitedmeows.meowlib.MeowLib.*;
import pisi.unitedmeows.meowlib.async.Future;
import pisi.unitedmeows.meowlib.async.Task;

import pisi.unitedmeows.meowlib.clazz.onion;
import pisi.unitedmeows.meowlib.clazz.prop;
import pisi.unitedmeows.meowlib.etc.CoID;

import pisi.unitedmeows.meowlib.ex.impl.NotFoundEx;
import pisi.unitedmeows.meowlib.memory.MemoryReader;
import pisi.unitedmeows.meowlib.memory.MemoryWriter;
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.client.WTcpClient;
import pisi.unitedmeows.meowlib.network.client.events.DCDataReceived;
import pisi.unitedmeows.meowlib.network.server.SocketClient;
import pisi.unitedmeows.meowlib.network.server.WCTcpTunnel;
import pisi.unitedmeows.meowlib.network.server.WTcpClientPool;
import pisi.unitedmeows.meowlib.network.server.events.DSClientQuit;
import pisi.unitedmeows.meowlib.network.server.events.DSDataReceived;
import pisi.unitedmeows.meowlib.network.server.events.DSConnectionRequest;
import pisi.unitedmeows.meowlib.network.server.WTcpServer;
import pisi.unitedmeows.meowlib.signal.Signal;
import pisi.unitedmeows.meowlib.thread.kThread;

public class Start {

    public static void main(String[] args) {
        WTcpServer server = new WTcpServer(IPAddress.LOOPBACK, 2173);

        server.listen();

        server.dataReceivedEvent.bind(new DSDataReceived() {
            @Override
            public void onDataReceived(SocketClient client, byte[] data) {
                System.out.println("SERVER << " + new String(data) );
            }
        });

        server.connectionRequestEvent.bind(new DSConnectionRequest() {
            @Override
            public void onClientConnecting(SocketChannel client) {
                System.out.println("Someone joined");
            }
        });



        WTcpClient client = new WTcpClient();
        client.connect(IPAddress.LOOPBACK, 2173);

        client.send("kes TEST MESSAGE 1".getBytes());
        client.send("TEST MESSAGE".getBytes());
        client.send("TEST MESSAGE 3".getBytes());
        client.send("TEST MESSAGE 4".getBytes());
        client.send("TEST MESSAGE 5".getBytes());
        client.send("TEST MESSAGE 6".getBytes());

        for (int i = 10; i > 0; i--) {
            WTcpClient client2 = new WTcpClient();
            client2.connect(IPAddress.LOOPBACK, 2173);
            client2.send(("CLIENT " + i).getBytes());
        }
        

    }




}
