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
                        System.out.println("transfering queue to another taskworker");
                        taskWorker.transferWork(freeWorker);
                    }
                }

                long checkBusyTime = (long) MeowLib.settings().get(MLibSettings.ASYNC_CHECK_BUSY).getValue();
                kThread.sleep(checkBusyTime);
                List<TaskWorker> freeWorkers = new ArrayList<>();
                for (TaskWorker taskWorker : threadPool) {
                    if (taskWorker.isFree() && taskWorker.queueSize() == 0) {
                        freeWorkers.add(taskWorker);
                    }
                }

                if (freeWorkers.size() == workerCount()) {
                    freeWorkers.remove(0);

                }
                if (!freeWorkers.isEmpty()) {
                    for (TaskWorker freeWorker : freeWorkers) {
                        if (freeWorker.queueSize() == 0) {
                            threadPool.remove(freeWorker);
                            freeWorker.stopWorker();
                        }
                    }
                }

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
        System.out.println("Creating new taskworker");
        taskWorker.startWorker();
        return taskWorker;
    }

    public static int workerCount() {
        return threadPool.size();
    }
}
