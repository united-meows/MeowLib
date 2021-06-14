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
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

import static pisi.unitedmeows.meowlib.async.Async.*;

public class WTcpClient {

    private Socket socket;

    public event<DCDataReceived> dataReceivedEvent = new event<>();
    private byte[] BUFFER = new byte[4096 * 2];
    private Thread receiveThread;
    private InputStream inputStream;
    private OutputStream outputStream;


    private Queue<byte[]> writeQueue;

    private Promise writePromise;


    private boolean keepAlive;
    private int keepAliveInterval = 20000;
    private Promise keepAlivePromise;

    public WTcpClient(/* proxy support ? */) {
        socket = new Socket();
        writeQueue = new ArrayDeque<>();
    }

    public WTcpClient connect(IPAddress ipAddress, int port) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress.getAddress());
            final SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
            socket.connect(socketAddress);
        } catch (IOException e) {
            /* find a better way for exceptions */
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
                //todo:
            }
        }
        if (keepAlivePromise != null) {
            keepAlivePromise.stop();
        }
        if (keepAlive) {
            keepAlivePromise = Async.async_loop(x -> {
                // send keepalive
                if (socket.isConnected()) {
                    send(NetworkConstants.KEEPALIVE_DATA);
                }
            }, keepAliveInterval);
        }

        if (writePromise != null) {
            writePromise.stop();
        }

        async_when(this::isConnected, u -> {
            writePromise = async_loop(x -> {

                if (!writeQueue.isEmpty()) {
                    byte[] current = writeQueue.poll();
                    try {
                        outputStream.write(current);

                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }, 1);
        }, 200);


        receiveThread = new Thread(this::receive);
        receiveThread.start();
        return this;
    }

    public void send(byte[] data) {
        writeQueue.add(data);
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    private void receive() {
        while (socket.isConnected() && !socket.isClosed()) {
            try {

                int size = inputStream.read(BUFFER);
                byte[] data = Arrays.copyOf(BUFFER, size);
                // received
                dataReceivedEvent.run((Object) data);

            } catch (Exception ex) {}
        }
    }

    public void close() {
        dataReceivedEvent.unbindAll();
        keepAlivePromise.stop();
        writeQueue.clear();
        writePromise.stop();
        //tm anladim
        try {
            socket.close();
        } catch (IOException e) {

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

    public Socket socket() {
        return socket;
    }
}
