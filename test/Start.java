package test;

import static pisi.unitedmeows.meowlib.async.Async.*;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

import pisi.unitedmeows.meowlib.async.Future;
import pisi.unitedmeows.meowlib.async.Task;

import pisi.unitedmeows.meowlib.clazz.onion;
import pisi.unitedmeows.meowlib.clazz.prop;
import pisi.unitedmeows.meowlib.etc.CoID;

import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.client.WTcpClient;
import pisi.unitedmeows.meowlib.network.client.events.DCDataReceived;
import pisi.unitedmeows.meowlib.network.server.events.DSClientQuit;
import pisi.unitedmeows.meowlib.network.server.events.DSDataReceived;
import pisi.unitedmeows.meowlib.network.server.events.DSConnectionRequest;
import pisi.unitedmeows.meowlib.network.server.WTcpServer;
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
        wTcpServer.setKeepAlive(true).setMaxKeepAliveInterval(1000);
        wTcpServer.listen();

        wTcpServer.connectionRequestEvent.bind(new DSConnectionRequest() {
            @Override
            public void onClientConnecting(SocketChannel client) {
                System.out.println("Connection received");
            }
        });

        wTcpServer.dataReceivedEvent.bind(new DSDataReceived() {
            @Override
            public void onDataReceived(SocketChannel client, ByteBuffer data) {
                System.out.println(new String(data.array()));
                wTcpServer.send(client, "meowlib".getBytes());
            }
        });

        wTcpServer.clientQuitEvent.bind(new DSClientQuit() {
            @Override
            public void onClientQuit(SocketChannel client) {
                System.out.println("client kicked");
            }
        });
        kThread.sleep(1000);

        WTcpClient client = new WTcpClient();
        client.setKeepAlive(true).setKeepAliveInterval(500);

        client.connect(IPAddress.LOOPBACK, 2174);
        kThread.sleep(1000);
        client.dataReceivedEvent.bind(new DCDataReceived() {
            @Override
            public void onDataReceived(byte[] data) {
                System.out.println(new String(data));
            }
        });
        client.send("hello world".getBytes());


    }


    public static void asyncTest(CoID id) {
        Task<?> task = task(id);
        kThread.sleep(new Random().nextInt(3000));
        task._return("test " + new Random().nextInt(120));
    }
}
