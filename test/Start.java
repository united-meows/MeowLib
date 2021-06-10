package test;

import static pisi.unitedmeows.meowlib.async.Async.*;

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
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.client.WTcpClient;
import pisi.unitedmeows.meowlib.network.client.events.DCDataReceived;
import pisi.unitedmeows.meowlib.network.server.WCTcpTunnel;
import pisi.unitedmeows.meowlib.network.server.WTcpClientPool;
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


    public static void main(String[] args) {
    }



    static List<String> players = new ArrayList<>();



    public static void asyncTest(CoID id) {
        Task<?> task = task(id);
        kThread.sleep(new Random().nextInt(3000));
        task._return("test " + new Random().nextInt(120));
    }
}
