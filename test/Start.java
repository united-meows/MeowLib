package test;

import static pisi.unitedmeows.meowlib.async.Async.*;
import static pisi.unitedmeows.meowlib.MeowLib.*;

import java.util.UUID;

import pisi.unitedmeows.meowlib.async.Task;
import pisi.unitedmeows.meowlib.etc.CoID;
import pisi.unitedmeows.meowlib.lists.MeowList;
import pisi.unitedmeows.meowlib.thread.kThread;
import pisi.unitedmeows.meowlib.variables.ubyte;
import pisi.unitedmeows.meowlib.variables.uint;

public class Start {


    public static void main(String[] args) {
        for (int i = 400; i > 0; i--) {
            CoID coid = CoID.generate();
            System.out.println(coid.toString());
        }
    }

    public static void asyncTest(UUID uuid) {
        Task<?> task = task(uuid);
        task.setResult("test");
        kThread.sleep(1000);
        System.out.println("Hello World");
    }
}
