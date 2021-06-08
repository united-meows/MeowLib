package pisi.unitedmeows.meowlib.network.client.events;

import pisi.unitedmeows.meowlib.clazz.delegate;

public abstract class DCDataReceived implements delegate {
    public abstract void onDataReceived(byte[] data);
}
