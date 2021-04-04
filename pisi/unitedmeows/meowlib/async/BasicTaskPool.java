package pisi.unitedmeows.meowlib.async;

import com.sun.jmx.remote.internal.ArrayQueue;
import pisi.unitedmeows.meowlib.MeowLib;
import pisi.unitedmeows.meowlib.etc.MLibSettings;
import pisi.unitedmeows.meowlib.thread.kThread;

import java.util.*;
import java.util.concurrent.*;

public class BasicTaskPool implements ITaskPool {


    private HashMap<Task<?>, Long> waitQueue;
    private List<TaskWorker> taskWorkers;
    private LinkedBlockingDeque<Task<?>> taskQueue;
    private Thread workerCThread;

    @Override
    public void setup() {
        taskWorkers = new ArrayList<>();
        taskQueue = new LinkedBlockingDeque<Task<?>>();
        waitQueue = new HashMap<>();
        workerCThread = new Thread(this::workerC);
        workerCThread.start();

        for (int i = 0; i < 5; i++) {
            TaskWorker taskWorker = new TaskWorker(this);
            taskWorkers.add(taskWorker);
            taskWorker.startWorker();
        }

    }

    public void workerC() {
        while (true) {
            boolean allBusy = true;
            List<TaskWorker> nWorkings = new ArrayList<>();
            long nWorkingTime = (long) MeowLib.settings().get(MLibSettings.ASYNC_NWORKING_TIME).getValue();
            for (TaskWorker worker : taskWorkers) {
                if (!worker.isBusy()) {
                    allBusy = false;
                } else if (worker.lastWorkTimeElapsed() > nWorkingTime) {
                    nWorkings.add(worker);
                }
            }
            
            if (taskQueue.size() > workerCount() * 20) {
                allBusy = true;
            }

            if (allBusy) {
                TaskWorker freeWorker = new TaskWorker(this);
                taskWorkers.add(freeWorker);
                freeWorker.startWorker();
            } else if (taskWorkers.size() > 6){
                for (TaskWorker nWorking : nWorkings) {
                    taskWorkers.remove(nWorking);
                }
            }

            final long currentTime = System.currentTimeMillis();
            List<Task<?>> addQueue = new ArrayList<>();
            for (Map.Entry<Task<?>, Long> waitQEntry : waitQueue.entrySet()) {
                if (currentTime >= waitQEntry.getValue()) {
                    addQueue.add(waitQEntry.getKey());
                    queue(waitQEntry.getKey());
                }
            }

            for (Task<?> task : addQueue) {
                waitQueue.remove(task);
            }

            kThread.sleep((long) MeowLib.settings().get(MLibSettings.ASYNC_CHECK_BUSY).getValue());
        }
    }

    @Override
    public void queue(Task<?> task) {
        taskQueue.add(task);
    }

    @Override
    public void queue_f(Task<?> task) {
        taskQueue.addFirst(task);
    }

    @Override
    public void queue_w(Task<?> task, long after) {
        waitQueue.put(task, System.currentTimeMillis() + after);
    }

    @Override
    public Task<?> poll() {
        if (!taskQueue.isEmpty()) {
            return taskQueue.poll();
        }

        long waitTime = (long)MeowLib.settings().get(MLibSettings.ASYNC_POLL_WAIT_DELAY).getValue();

        while (taskQueue.isEmpty()) {
            kThread.sleep(waitTime);
        }

        return taskQueue.poll();
    }

    @Override
    public int workerCount() {
        return taskWorkers.size();
    }

    @Override
    public void close() {
        taskQueue.clear();
        for (TaskWorker taskWorker : taskWorkers) {
            taskWorker.stopWorker();
        }
        taskWorkers.clear();
    }

}
