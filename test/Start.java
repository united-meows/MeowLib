package test;

import static pisi.unitedmeows.meowlib.async.Async.*;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
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
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.client.WTcpClient;
import pisi.unitedmeows.meowlib.network.client.events.DCDataReceived;
import pisi.unitedmeows.meowlib.network.server.WCTcpTunnel;
import pisi.unitedmeows.meowlib.network.server.events.DSClientQuit;
import pisi.unitedmeows.meowlib.network.server.events.DSDataReceived;
import pisi.unitedmeows.meowlib.network.server.events.DSConnectionRequest;
import pisi.unitedmeows.meowlib.network.server.WTcpServer;
import pisi.unitedmeows.meowlib.signal.Signal;
import pisi.unitedmeows.meowlib.thread.kThread;

public class Start {

    private static Future<?> future;


    private static prop<Integer> number = new prop<Integer>(0) {
        @Override
        public void set(Integer value) {
            /* do something */
            this.value = value;
        }

        @Override
        public Integer get() {
            value += 1;
            return this.value;
        }
    };

    public static onion<String> text = new onion<>();

    static boolean ipanaMalmi;


    public static void main(String[] args) {
        WTcpServer wTcpServer = new WTcpServer(IPAddress.LOOPBACK, 2174);
        wTcpServer.listen();

        final SocketChannel channel1, channel2;


        WTcpClient client1 = new WTcpClient();
        client1.dataReceivedEvent.bind(new DCDataReceived() {
            @Override
            public void onDataReceived(byte[] data) {
                System.out.println("Client1 << " + new String(data));
            }
        });
        client1.connect(IPAddress.LOOPBACK, 2174);

        WTcpClient client2 = new WTcpClient();
        client2.dataReceivedEvent.bind(new DCDataReceived() {
            @Override
            public void onDataReceived(byte[] data) {
                System.out.println("Client2 << " + new String(data));
            }
        });
        client2.connect(IPAddress.LOOPBACK, 2174);



        while (wTcpServer.connectedClients().size() < 2) {
            kThread.sleep(500);
        }

        WCTcpTunnel wcTcpTunnel = new WCTcpTunnel(wTcpServer, wTcpServer.connectedClients().get(0), wTcpServer.connectedClients().get(1));

        kThread.sleep(1000);
        client1.send("Hello from the other side".getBytes());
        client2.send("Hi".getBytes());

    }



    static List<String> players = new ArrayList<>();



    public static void asyncTest(CoID id) {
        Task<?> task = task(id);
        kThread.sleep(new Random().nextInt(3000));
        task._return("test " + new Random().nextInt(120));
    }
}
