package test;

import pisi.unitedmeows.meowlib.MeowLib;
import static pisi.unitedmeows.meowlib.async.Async.*;

import pisi.unitedmeows.meowlib.async.Task;
import pisi.unitedmeows.meowlib.async.TaskPool;
import pisi.unitedmeows.meowlib.thread.kThread;
import sun.nio.ch.ThreadPool;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import pisi.unitedmeows.meowlib.variables.ubyte;

public class Start {

    public static void main(String[] args) {

        // byte can only store up to 127
        ubyte test = new ubyte((byte)10);
        ubyte test2 = new ubyte((byte)127);
        test2.plus(test);
        System.out.println(test2.raw());



    }

    public static void testAsyncMethod(UUID taskId) {
        task(taskId).setResult("Hello World");
    }

}
