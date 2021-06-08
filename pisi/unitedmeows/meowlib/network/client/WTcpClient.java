package pisi.unitedmeows.meowlib.network.client;

import pisi.unitedmeows.meowlib.async.Async;
import pisi.unitedmeows.meowlib.async.Promise;
import pisi.unitedmeows.meowlib.clazz.event;
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.NetworkConstants;
import pisi.unitedmeows.meowlib.network.client.events.DCDataReceived;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class WTcpClient {

    private Socket socket;

    public event<DCDataReceived> dataReceivedEvent = new event<>();
    private byte[] BUFFER = new byte[4096 * 2];
    private Thread receiveThread;
    private InputStream inputStream;
    private OutputStream outputStream;

    private boolean keepAlive;
    private int keepAliveInterval = 20000;
    private Promise keepAlivePromise;

    public WTcpClient() {
        socket = new Socket();
    }

    public WTcpClient connect(IPAddress ipAddress, int port) {
        try {
            InetAddress inetAddress= InetAddress.getByName(ipAddress.getAddress());
            final SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
            socket.connect(socketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {

        }

        if (receiveThread != null) {
            try {
                receiveThread.stop();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (keepAlivePromise != null) {
            keepAlivePromise.stop();
        }
        keepAlivePromise = Async.async_loop(x-> {
            // send keepalive
            send(NetworkConstants.KEEPALIVE_DATA);
        }, keepAliveInterval);

        receiveThread = new Thread(this::receive);
        receiveThread.start();
        return this;
    }

    public void send(byte[] data) {
        try {
            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive() {
        while (socket.isConnected() && !socket.isClosed()) {
            try {

                int size = inputStream.read(BUFFER);
                byte[] data = Arrays.copyOf(BUFFER, size);
                // received
                dataReceivedEvent.run(data);

            } catch (Exception ex) {}
        }
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public WTcpClient setKeepAliveInterval(int keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
        return this;
    }

    public WTcpClient setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }
}