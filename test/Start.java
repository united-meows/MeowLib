package test;

import static pisi.unitedmeows.meowlib.async.Async.*;

import java.util.UUID;

import pisi.unitedmeows.meowlib.MeowLib;
import pisi.unitedmeows.meowlib.async.Task;
import pisi.unitedmeows.meowlib.etc.MeowList;
import pisi.unitedmeows.meowlib.filesystem.kFile;
import pisi.unitedmeows.meowlib.thread.kThread;
import pisi.unitedmeows.meowlib.variables.ubyte;
import static pisi.unitedmeows.meowlib.MeowLib.*;
public class Start {

    public static void main(String[] args) {
        String result = (String) await(async((u)-> asyncTest(u))).result();
        System.out.println(result);
    }

    public static void asyncTest(UUID uuid) {
        Task<?> task = task(uuid);
        task.setResult("I HATE ASYNC");
    }
}
