package pisi.unitedmeows.meowlib.async;

import pisi.unitedmeows.meowlib.etc.CoID;

public class Future<X> {

    private X value;
    private CoID pointer;
    private Task task;

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

    public <x> x await() {
        return (x) Async.await(this);
    }


    public CoID pointer() {
        return pointer;
    }
}
