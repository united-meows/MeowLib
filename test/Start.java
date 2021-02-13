package test;

import pisi.unitedmeows.meowlib.MeowLib;
import static pisi.unitedmeows.meowlib.async.Async.*;

import pisi.unitedmeows.meowlib.async.Task;
import pisi.unitedmeows.meowlib.async.TaskPool;
import pisi.unitedmeows.meowlib.thread.kThread;

public class Start {

    public static void main(String[] args) {
        async(()-> {System.out.println("test1"); kThread.sleep(10000);return null;});
        async(()-> {System.out.println("test2"); kThread.sleep(4000);return null;});
        async(()-> {System.out.println("test3"); kThread.sleep(200);return null;});
        async(()-> {System.out.println("test4");kThread.sleep(500); return null;});
        async(()-> {System.out.println("test1"); kThread.sleep(10000);return null;});
        async(()-> {System.out.println("test2"); kThread.sleep(4000);return null;});
        

    }

}
