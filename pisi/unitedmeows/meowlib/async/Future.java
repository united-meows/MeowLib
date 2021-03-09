package pisi.unitedmeows.meowlib.async;

import pisi.unitedmeows.meowlib.etc.CoID;

import java.util.ArrayList;
import java.util.List;

public class Future<X> {

    private X value;
    private CoID pointer;
    private Task task;
    private transient List<IAsyncAction> afterTasks;

    public Future(CoID pointer) {
        this.pointer = pointer;
        task = null;
    }

    public Task<X> task() {
        if (task == null) {
            task = Async.task(pointer);
        }
        return task;
    }

    public void post() {
        if (afterTasks != null) {
            for (IAsyncAction afterTask : afterTasks) {
                Async.async(afterTask);
            }
            afterTasks.clear();
        }
        task = Async.task(pointer);
        Async.removePointer(pointer);
    }

    public void after(IAsyncAction task) {
        if (afterTasks == null) {
            afterTasks = new ArrayList<>();
        }
        afterTasks.add(task);
    }


    public <x> x await() {
        return (x) Async.await(this);
    }


    public CoID pointer() {
        return pointer;
    }
}
