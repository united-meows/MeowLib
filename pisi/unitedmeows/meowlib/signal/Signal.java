package pisi.unitedmeows.meowlib.signal;

import static pisi.unitedmeows.meowlib.async.Async.*;

import pisi.unitedmeows.meowlib.clazz.prop;
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.client.WTcpClient;
import pisi.unitedmeows.meowlib.thread.kThread;

import java.util.ArrayList;
import java.util.List;

public class Signal {


    /*TODO: USE STELIX TO SEND DATA */
    /* SXF 2.0 */

    /* 2950 - 3100 */


    /* change the integer later */
    /* Maybe SoftwareInfo ? */
    public static List<Integer> discover(IPAddress address) {
        List<Integer> ports = new ArrayList<>();

        for (int port = 2950; port < 3100; port++) {
            WTcpClient client = new WTcpClient();
            client.connect(address, port);
            if (client.isConnected()) {
                ports.add(port);
            }
        }

        return ports;
    }

    @Deprecated
    @SuppressWarnings("buggy")
    public static List<Integer> discover_fast(IPAddress address) {
        final List<Integer> ports = new ArrayList<>();

        prop<Integer> runningTasks = new prop<>(0);

        for (int port = 2950; port < 3100; port++) {
            int finalPort = port;
            runningTasks.set(runningTasks.get() + 1);
            async(u-> {
                WTcpClient client = new WTcpClient();
                client.connect(address, finalPort);
                if (client.isConnected()) {
                    ports.add(finalPort);
                }
                runningTasks.set(runningTasks.get() -1);
            });
        }

        while (runningTasks.get() > 0) {
            kThread.sleep(10);
        }

        return ports;
    }


}
