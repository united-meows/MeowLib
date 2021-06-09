package pisi.unitedmeows.meowlib.network.server.events;

import pisi.unitedmeows.meowlib.clazz.delegate;
import pisi.unitedmeows.meowlib.network.client.WTcpClient;

import java.nio.channels.SocketChannel;

public abstract class DTCDataSendRequest implements delegate {
    public abstract void onDataSendRequest();

    public static class Args {
        public boolean canceled = false;
    }
}
