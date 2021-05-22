package test;

import static pisi.unitedmeows.meowlib.async.Async.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import pisi.unitedmeows.meowlib.async.Future;
import pisi.unitedmeows.meowlib.async.Promise;
import pisi.unitedmeows.meowlib.async.Task;

import pisi.unitedmeows.meowlib.clazz.onion;
import pisi.unitedmeows.meowlib.clazz.prop;
import pisi.unitedmeows.meowlib.clazz.type;
import pisi.unitedmeows.meowlib.etc.CoID;

import static pisi.unitedmeows.meowlib.lists.list.*;
import pisi.unitedmeows.meowlib.math.MeowMath;
import static pisi.unitedmeows.meowlib.predefined.STRING.*;
import pisi.unitedmeows.meowlib.thread.kThread;

public class Start {

    private static Future<?> future;

    private static prop<Integer> number = new prop<Integer>(0) {
        @Override
        public void set(Integer value) {
            /* do something */
            this.value = value;
        }

        @Override
        public Integer get() {
            value += 1;
            return this.value;
        }
    };

    public static onion<String> text = new onion<>();

    static int i;
    public static void main(String[] args) {

     }


    public static void asyncTest(CoID id) {
        Task<?> task = task(id);
        kThread.sleep(new Random().nextInt(3000));
        task._return("test " + new Random().nextInt(120));
    }
}
