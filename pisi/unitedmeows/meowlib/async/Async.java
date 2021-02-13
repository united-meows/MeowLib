package pisi.unitedmeows.meowlib.async;

import pisi.unitedmeows.meowlib.etc.IAction;

public class Async {

    public static Task<?> async(IAction action) {
        Task<Object> task = new Task<Object>() {
            @Override
            public Object run() {
                return action.run();
            }
        }
    }

}
