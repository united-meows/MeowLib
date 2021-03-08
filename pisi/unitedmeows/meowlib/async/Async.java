package pisi.unitedmeows.meowlib.async;

import pisi.unitedmeows.meowlib.MeowLib;
import pisi.unitedmeows.meowlib.etc.CoID;
import pisi.unitedmeows.meowlib.etc.IAction;
import pisi.unitedmeows.meowlib.etc.MLibSettings;
import pisi.unitedmeows.meowlib.thread.kThread;

import java.util.HashMap;
import java.util.UUID;

public class Async {

    /*TODO: Create ForgettableHashmap and replace this */
    private static HashMap<CoID, Task<?>> pointers;

    static {
        pointers = new HashMap<>();
    }

    public static Task<?> task(CoID id) {
        return pointers.get(id);
    }

    public static void _return(CoID uuid, Object result) {
        task(uuid)._return(result);
    }


    public static <X> Future<X> async_f(IAsyncAction action) {
        // change this uuid alternative
        final CoID pointer = newPointer();

        Task<?> task = new Task<Object>(action) {
            @Override
            public void run() {
                action.start(pointer);
            }
        };

        pointers.put(pointer, task);
        MeowLib.getTaskPool().queue_f(task);
        return new Future<>(pointer);
    }

    public static <X> Future<X> async(IAsyncAction action) {
        // change this uuid alternative
        final CoID pointer = newPointer();


        Task<?> task = new Task<Object>(action) {
            @Override
            public void run() {
               action.start(pointer);
            }
        };

        pointers.put(pointer, task);
        MeowLib.getTaskPool().queue(task);
        return new Future<>(pointer);
    }



    public static <X> X await(Future<?> future) {
        return (X) await(pointers.get(future.pointer())).result();
    }

    public static Task<?> await(Task<?> task) {
        long checkTime = (long)MeowLib.settings().get(MLibSettings.ASYNC_AWAIT_CHECK_DELAY).getValue();
        while (task.state() == Task.State.RUNNING || task.state() == Task.State.IDLE) {
            kThread.sleep(checkTime);
        }
        return task;
    }


    /* this code shouldn't exists but looks cool */
    public static <X> Future<X> async_t(IAsyncAction action) {
        final CoID pointer = newPointer();
        Task<?> task = new Task<Object>(action) {
            @Override
            public void run() {
                action.start(pointer);
            }
        };

        pointers.put(pointer, task);
        new Thread(()-> {
            task.pre();
            task.run();
            task.post();
        }).start();

        return new Future<X>(pointer);
    }

    private static CoID newPointer() {
        CoID pointer;
        do {
            pointer = CoID.generate();
        } while (pointers.containsKey(pointer));
        return pointer;
    }


}
