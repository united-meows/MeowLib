package pisi.unitedmeows.meowlib.async;

import pisi.unitedmeows.meowlib.MeowLib;
import pisi.unitedmeows.meowlib.etc.MLibSettings;
import pisi.unitedmeows.meowlib.thread.kThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class TaskPool {

    private static List<TaskWorker> threadPool;
    private static List<TaskWorker> freeWorkers;
    private static Thread workerCThread;

    static {
        threadPool = new ArrayList<>();
        startWorkerController();
        setup();
    }

    private static void startWorkerController() {
        workerCThread = new Thread(() -> {
            while (true) {
                long maxRunTime = (long) MeowLib.settings().get(MLibSettings.ASYNC_WORKER_BUSY).getValue();
                for (TaskWorker taskWorker : threadPool) {
                    if (taskWorker.isWorking()) {
                        long workingTime = taskWorker.getRunningTask().timeElapsed();
                        /* not using 'taskWorker.isBusy()' because we don't need to get ASYNC_WORKER_BUSY
                         * every time */
                        if (workingTime >= maxRunTime) {
                            taskWorker.transferWork(freeWorker());
                        }
                    }
                }

                long checkBusyTime = (long) MeowLib.settings().get(MLibSettings.ASYNC_CHECK_BUSY).getValue();
                kThread.sleep(checkBusyTime);
            }
        });
    }

    private static void setup() {
        for (int i = 5; i > 0; i--) {

        }
    }

    public static TaskWorker freeWorker() {
        for (TaskWorker taskWorker : threadPool) {
            if (taskWorker.isBusy()) {
                continue;
            }
            return taskWorker;
        }

        TaskWorker taskWorker = new TaskWorker();
        threadPool.add(taskWorker);
        return taskWorker;
    }


}
