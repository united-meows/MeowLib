package test;

import pisi.unitedmeows.meowlib.MeowLib;
import static pisi.unitedmeows.meowlib.async.Async.*;

import pisi.unitedmeows.meowlib.async.Task;
import pisi.unitedmeows.meowlib.async.TaskPool;
import pisi.unitedmeows.meowlib.thread.kThread;

public class Start {

    public static void main(String[] args) {
        async((u)-> {System.out.println("test1");});
        await(async((u)->
        {
            System.out.println("test2");
        }
        ));

        await(async((u)-> {System.out.println("test3");}));
        

    }

}
