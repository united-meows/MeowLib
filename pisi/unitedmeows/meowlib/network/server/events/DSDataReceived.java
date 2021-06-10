package pisi.unitedmeows.meowlib.network.server.events;

import pisi.unitedmeows.meowlib.clazz.delegate;
import pisi.unitedmeows.meowlib.network.server.SocketClient;


public abstract class DSDataReceived implements delegate {
    public abstract void onDataReceived(SocketClient client, byte[] data);
}
