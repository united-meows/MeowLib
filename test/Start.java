package test;

import static pisi.unitedmeows.meowlib.async.Async.*;

import java.util.UUID;

import pisi.unitedmeows.meowlib.async.Task;
import pisi.unitedmeows.meowlib.thread.kThread;

public class Start {


    public static void main(String[] args) {
        String result = (String) await(async((u) -> asyncTest(u))).result();
        System.out.println(result);
        async((u)-> System.out.println("hello world")).result();
        async((u)-> System.out.println("hello world"));
        async((u)-> {System.out.println("hello world"); kThread.sleep(3000);});
        async_f((u)-> System.out.println("hello world FAST #1"));
        async((u)-> System.out.println("hello world"));
        async((u)-> System.out.println("hello world"));
        async((u)-> System.out.println("hello world"));
        async((u)-> System.out.println("hello world"));
        async_f((u)-> System.out.println("hello world FAST #2"));
        async((u)-> System.out.println("hello world"));


    }

    public static void asyncTest(UUID uuid) {
        Task<?> task = task(uuid);
        task.setResult("test");
        kThread.sleep(1000);
        System.out.println("Hello World");
    }
}
