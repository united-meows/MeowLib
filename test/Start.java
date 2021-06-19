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
import pisi.unitedmeows.meowlib.clazz.updatable;
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
import pisi.unitedmeows.meowlib.signal.SignalApp;
import pisi.unitedmeows.meowlib.signal.SignalServer;
import pisi.unitedmeows.meowlib.thread.kThread;

public class Start {

    private static final updatable<String> variable = new updatable<String>("", 200) {
        @Override
        public void update() {
            value += "e";
        }
    };

    // i hate java
    public static void main(String[] args) throws InterruptedException {
        while (true) {
            System.out.println(variable.get());
            kThread.sleep(300);
        }
    }

}
