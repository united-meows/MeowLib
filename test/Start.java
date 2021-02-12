package test;

import pisi.unitedmeows.meowlib.async.TaskPool;
import pisi.unitedmeows.meowlib.thread.kThread;

public class Start {

    public static void main(String[] args) {
        System.out.println("test 1");
        kThread.sleep(1000);
        System.out.println("test 2");
        TaskPool.test2();
    }
}
