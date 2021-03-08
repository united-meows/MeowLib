package test;

import static pisi.unitedmeows.meowlib.async.Async.*;
import static pisi.unitedmeows.meowlib.MeowLib.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import pisi.unitedmeows.meowlib.async.Future;
import pisi.unitedmeows.meowlib.async.Task;
import pisi.unitedmeows.meowlib.etc.CoID;
import pisi.unitedmeows.meowlib.lists.MeowList;
import pisi.unitedmeows.meowlib.thread.kThread;
import pisi.unitedmeows.meowlib.variables.ubyte;
import pisi.unitedmeows.meowlib.variables.uint;

public class Start {


    public static void main(String[] args) {

        // usage 1 (not cool)
       String usage1_0 = async((u)->asyncTest(u)).await();

       // usage 2
       String usage2_0 = await(async((u)->asyncTest(u)));
       String usage2_1 = await(async(Start::asyncTest));



       Future<String> result = async(Start::asyncTest);
       while (result.task().state() != Task.State.FINISHED) {
           System.out.println("TEST: "+ result.task().timeElapsed());
           kThread.sleep(10);
       }

       System.out.println(usage1_0);
       System.out.println(usage2_0);
       System.out.println(usage2_1);
    }

    public static void asyncTest(CoID id) {
        Task<?> task = task(id);
        kThread.sleep(new Random().nextInt(3000));
        task._return("test " + new Random().nextInt(120));
    }
}
