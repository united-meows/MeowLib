package pisi.unitedmeows.meowlib.async;

import pisi.unitedmeows.meowlib.MeowLib;
import pisi.unitedmeows.meowlib.etc.MLibSettings;
import pisi.unitedmeows.meowlib.thread.kThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class TaskPool {

    private static CopyOnWriteArrayList<TaskWorker> threadPool;
    private static List<TaskWorker> freeWorkers;
    private static Thread workerCThread;

    static {
        threadPool = new CopyOnWriteArrayList<>();
        startWorkerController();
        setup();
    }

    private static void startWorkerController() {
        workerCThread = new Thread(() -> {
            while (true) {
                for (TaskWorker taskWorker : threadPool) {
                    if (taskWorker.isBusy() && taskWorker.queueSize() != 0) {
                        TaskWorker freeWorker = freeWorker();
                        taskWorker.transferWork(freeWorker);
                    }
                }

                long checkBusyTime = (long) MeowLib.settings().get(MLibSettings.ASYNC_CHECK_BUSY).getValue();
                kThread.sleep(checkBusyTime);
            }
        });
        workerCThread.start();
    }

    private static void setup() {
        for (int i = 5; i > 0; i--) {

        }
    }

    public static TaskWorker freeWorker() {

        // selecting least taskworker that has least queue and not busy
        // ===========
        int leastQueue = -1;
        TaskWorker worker = null;
        for (TaskWorker taskWorker : threadPool) {
            if (taskWorker.isBusy()) {
                continue;
            }

            if (leastQueue == -1 || leastQueue >= taskWorker.queueSize()) {
                leastQueue = taskWorker.queueSize();
                worker = taskWorker;
            }
        }
        // if found return
        if (worker != null) {
            return worker;
        }
        // ============
        // if all taskworkers are busy
        // create a new taskworker
        TaskWorker taskWorker = new TaskWorker();
        threadPool.add(taskWorker);
        taskWorker.startWorker();
        return taskWorker;
    }


}
