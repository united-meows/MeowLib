package pisi.unitedmeows.meowlib.network.server.events;

import pisi.unitedmeows.meowlib.clazz.delegate;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public abstract class DSDataReceived implements delegate {
    public abstract void onDataReceived(SocketChannel client, byte[] data);
}
