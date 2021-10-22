package pisi.unitedmeows.meowlib.signal;

import static pisi.unitedmeows.meowlib.async.Async.*;

import pisi.unitedmeows.meowlib.etc.CoID;
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.client.WTcpClient;
import pisi.unitedmeows.meowlib.network.client.events.DCDataReceived;
import pisi.unitedmeows.meowlib.thread.kThread;
import stelix.xfile.SxfDataBlock;
import stelix.xfile.reader.SxfReader;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Signal {


    public static final byte[] GET_INFO = ";SIGNAL->GET_INFO".getBytes();

    /*TODO: USE STELIX TO SEND DATA */
    /* SXF 2.0 */

    /* 2950 - 3100 */



    public static List<SignalApp> discover(IPAddress address) {
        List<Integer> ports = discover_ports(address);
        List<SignalApp> apps = new ArrayList<>();

        for (int port : ports) {
            WTcpClient client = new WTcpClient();
            client.connect(address, port);
            if (client.isConnected()) {
                client.send(Signal.GET_INFO);
                client.dataReceivedEvent.bind(new DCDataReceived() {
                    @Override
                    public void onDataReceived(byte[] data) {
                        SxfDataBlock appInfo = SxfReader.readRaw(new String(data)).dataBlock("app");
                        SignalApp signalApp = new SignalApp(appInfo.variable("appName"), appInfo.variable("appVersion"), new CoID(appInfo.variable("appKey")));
                        apps.add(signalApp);
                    }
                });

            }
            kThread.sleep(1000);
            client.close();
        }
        return apps;
    }


    public static List<Integer> discover_ports(IPAddress address) {
        List<Integer> ports = new ArrayList<>();

        for (int port = 2950; port < 3100; port++) {
            if (available(port)) {
                ports.add(port);
            }
        }
        return ports;
    }

    @Deprecated
    public static List<Integer> discover_ports_fast(IPAddress address) {
        final List<Integer> ports = new ArrayList<>();
        for (int port = 2950; port < 3100; port++) {
            int finalPort = port;;
            async(u-> {

                if (available(finalPort)) {
                    ports.add(finalPort);
                }
            });
        }

        kThread.sleep(1000);
        return ports;
    }

    private static boolean available(int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", port));
            boolean result = socket.isConnected();
            socket.close();
            return result;
        } catch (Exception ex) {
            return false;
        }
    }


}
