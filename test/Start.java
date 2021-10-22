package test;

import pisi.unitedmeows.meowlib.memory.MemoryReader;
import pisi.unitedmeows.meowlib.memory.MemoryWriter;
import pisi.unitedmeows.meowlib.network.IPAddress;
import pisi.unitedmeows.meowlib.network.client.WTcpClient;
import pisi.unitedmeows.meowlib.network.server.WTcpServer;
import pisi.unitedmeows.meowlib.sql.DatabaseClient;
import pisi.unitedmeows.meowlib.thread.kThread;

import java.io.IOException;

public class Start {

    public static void main(String[] args) throws Exception {
        DatabaseClient databaseClient = new DatabaseClient("localhost", "root", "12345", "test");

    }

}