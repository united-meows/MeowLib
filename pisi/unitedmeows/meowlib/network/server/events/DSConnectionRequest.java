package pisi.unitedmeows.meowlib.network.server.events;

import pisi.unitedmeows.meowlib.clazz.delegate;

import java.nio.channels.SocketChannel;

public abstract class DConnectionRequest implements delegate {

    public abstract void onClientConnecting(SocketChannel client);
}
