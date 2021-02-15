package test;

import static pisi.unitedmeows.meowlib.async.Async.*;

import java.util.UUID;

import pisi.unitedmeows.meowlib.MeowLib;
import pisi.unitedmeows.meowlib.async.Task;
import pisi.unitedmeows.meowlib.thread.kThread;
import pisi.unitedmeows.meowlib.variables.ubyte;
import static pisi.unitedmeows.meowlib.MeowLib.*;
public class Start {

    public static void main(String[] args) {
        Task result = await(async((u)-> testAsyncMethod(u)));
        System.out.println((String)result.result());

    }

    public static void testAsyncMethod(UUID taskId) {
        task(taskId).setResult("Hello World");
        kThread.sleep(3000);
    }

}
