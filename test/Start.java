package test;

import pisi.unitedmeows.meowlib.MeowLib;
import static pisi.unitedmeows.meowlib.async.Async.*;

import pisi.unitedmeows.meowlib.async.Task;
import pisi.unitedmeows.meowlib.async.TaskPool;
import pisi.unitedmeows.meowlib.thread.kThread;

import java.util.UUID;

public class Start {

    public static void main(String[] args) {
        String result = (String) await(async((u) -> testAsyncMethod(u))).result();
        System.out.println(result);
        await(async((u)->
        {
            System.out.println("test2");
            kThread.sleep(1000);
        }
        ));
        async((u)-> {System.out.println("test1");});
        async((u)-> {System.out.println("test9");});
        await(async((u)-> { kThread.sleep(2000); System.out.println("test3");}));
        System.out.println("Test");

    }

    public static void testAsyncMethod(UUID taskId) {
        task(taskId).setResult("Hello World");
    }

}
