package pisi.unitedmeows.meowlib.async;

import pisi.unitedmeows.meowlib.MeowLib;
import pisi.unitedmeows.meowlib.etc.IAction;
import pisi.unitedmeows.meowlib.etc.MLibSettings;
import pisi.unitedmeows.meowlib.thread.kThread;

import java.util.HashMap;
import java.util.UUID;

public class Async {
    /* replace this to meowmap */
    private static HashMap<UUID, Task<?>> pointers;

    static {
        pointers = new HashMap<>();
    }

    public static Task<?> task(UUID uuid) {
        return pointers.get(uuid);
    }

    public static void taskResult(UUID uuid, Object result) {
        task(uuid).setResult(result);
    }


    public static Task<?> async_f(IAsyncAction action) {
        // change this uuid alternative
        final UUID pointer = newPointer();

        Task<?> task = new Task<Object>(action) {
            @Override
            public void run() {
                action.start(pointer);
            }
        };

        pointers.put(pointer, task);
        MeowLib.getTaskPool().queue_f(task);
        return task;
    }

    public static Task<?> async(IAsyncAction action) {
        // change this uuid alternative
        final UUID pointer = newPointer();


        Task<?> task = new Task<Object>(action) {
            @Override
            public void run() {
               action.start(pointer);
            }
        };

        pointers.put(pointer, task);
        MeowLib.getTaskPool().queue(task);
        return task;
    }



    public static Task<?> await(UUID uuid) {
        return await(pointers.get(uuid));
    }

    public static Task<?> await(Task task) {
        long checkTime = (long)MeowLib.settings().get(MLibSettings.ASYNC_AWAIT_CHECK_DELAY).getValue();
        while (task.state() == Task.State.RUNNING || task.state() == Task.State.IDLE) {
            kThread.sleep(checkTime);
        }
        return task;
    }


    /* this code shouldn't exists but looks cool */
    public static UUID async_t(IAsyncAction action) {
        final UUID pointer = UUID.randomUUID();
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

        return pointer;
    }

    private static UUID newPointer() {
        UUID pointer;
        do {
            pointer = UUID.randomUUID();
        } while (pointers.containsKey(pointer));
        return pointer;
    }


}
