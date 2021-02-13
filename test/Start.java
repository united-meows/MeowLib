package test;

import pisi.unitedmeows.meowlib.MeowLib;
import static pisi.unitedmeows.meowlib.async.Async.*;

import pisi.unitedmeows.meowlib.async.Task;
import pisi.unitedmeows.meowlib.async.TaskPool;
import pisi.unitedmeows.meowlib.thread.kThread;

public class Start {

    public static void main(String[] args) {
        System.out.println("test 1");
        kThread.sleep(1000);
        System.out.println("test 2");
        MeowLib.settings();
        async(new Task<Object>() {

        });
    }
}
