package pisi.unitedmeows.meowlib.network.server.events;

import pisi.unitedmeows.meowlib.clazz.delegate;
import pisi.unitedmeows.meowlib.network.server.SocketClient;

import java.nio.channels.SocketChannel;

public abstract class DSConnectionRequest implements delegate {

    public abstract void onClientConnecting(SocketClient client);
}
