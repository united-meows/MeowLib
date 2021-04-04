package test;

import static pisi.unitedmeows.meowlib.async.Async.*;
import static pisi.unitedmeows.meowlib.MeowLib.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import pisi.unitedmeows.meowlib.async.Future;
import pisi.unitedmeows.meowlib.async.Promise;
import pisi.unitedmeows.meowlib.async.Task;
import pisi.unitedmeows.meowlib.etc.CoID;
import pisi.unitedmeows.meowlib.lists.MeowList;
import pisi.unitedmeows.meowlib.thread.kThread;
import pisi.unitedmeows.meowlib.variables.ubyte;
import pisi.unitedmeows.meowlib.variables.uint;

public class Start {

    private static Future<?> future;

    public static void main(String[] args) {


        async((u)-> System.out.println("Usage 1"));
        async((u)-> System.out.println("Usage 2")).after((u)-> System.out.println("test"));

        async_w((u)-> System.out.println("Usage 3 (this will run after 5000ms)"), 5000);

        /* defining a variable is not required if you'll not start/stop the loop */
        final Promise promise = async_loop((u)-> System.out.println("Hello World"), 500, 3000);
    }


    public static void asyncTest(CoID id) {
        Task<?> task = task(id);
        kThread.sleep(new Random().nextInt(3000));
        task._return("test " + new Random().nextInt(120));
    }
}
