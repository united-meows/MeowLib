package pisi.unitedmeows.meowlib.async;

import pisi.unitedmeows.meowlib.etc.IAction;

public class Async {

    public static Task<?> async(IAsyncAction action) {
        Task<?> task = new Task<Object>(action, null) {
            @Override
            public Task<?> run() {
                return super.run();
            }
        };
        TaskPool.freeWorker().queue(task);
        return null;
    }


}
