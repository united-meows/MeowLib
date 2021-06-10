package pisi.unitedmeows.meowlib.network.server.events;

import pisi.unitedmeows.meowlib.clazz.delegate;
import pisi.unitedmeows.meowlib.network.server.SocketClient;

public abstract class DSClientQuit implements delegate {
    public abstract void onClientQuit(SocketClient client);
}
