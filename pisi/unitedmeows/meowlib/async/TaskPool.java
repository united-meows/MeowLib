package pisi.unitedmeows.meowlib.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskPool {

    private static List<TaskWorker> threadPool;

    static {
        threadPool = new ArrayList<>();
        setup();
    }

    private static void setup() {
        for (int i = 5; i > 0; i--) {

        }
    }


}
