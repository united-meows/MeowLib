package pisi.unitedmeows.meowlib.network.server.events;

import pisi.unitedmeows.meowlib.clazz.delegate;

import java.nio.channels.SocketChannel;

public abstract class DSClientQuit implements delegate {
    public abstract void onClientQuit(SocketChannel client);
}
