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

public class Start {

    public static void main(String[] args) {
        async((u) -> {
            while (true) {

                System.out.println("Worker count: " + TaskPool.workerCount());

                kThread.sleep(100);
            }
        });
        while (true) {
            async((u) -> {
                System.out.println("test");
                kThread.sleep(50);
            });
            kThread.sleep(10);
        }

    }

    public static void testAsyncMethod(UUID taskId) {
        task(taskId).setResult("Hello World");
    }

}
