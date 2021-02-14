package pisi.unitedmeows.meowlib.async;

import com.sun.istack.internal.NotNull;
import pisi.unitedmeows.meowlib.MeowLib;
import pisi.unitedmeows.meowlib.etc.MLibSettings;
import pisi.unitedmeows.meowlib.thread.kThread;

import java.util.ArrayDeque;
import java.util.Queue;

public class TaskWorker extends Thread {

    private Queue<Task<?>> taskQueue;
    private boolean running;
    private Task<?> runningTask;

    public TaskWorker(Queue<Task<?>> tasks) {
        taskQueue = tasks;
    }
    public TaskWorker() {
        taskQueue = new ArrayDeque<Task<?>>();
    }

    @Override
    public void run() {
        while (running) {
            if (taskQueue.isEmpty()) {
                kThread.sleep((long) MeowLib.settings().get(MLibSettings.ASYNC_WAIT_DELAY).getValue());
                return;
            }
            try {
                runningTask = taskQueue.poll();
                runningTask.pre();
                runningTask.run();
                runningTask.post();
                runningTask = null;
            } catch (NoSuchMethodError | NullPointerException ex) {
                runningTask = null;
            }
        }
    }

    public void clear() {
        taskQueue.clear();
    }

    public int queueSize() {
        return taskQueue.size();
    }


    public void transferWork(@NotNull TaskWorker otherWorker) {
        if (taskQueue.isEmpty()) {
            return;
        }

        while (!taskQueue.isEmpty()) {
            otherWorker.queue(taskQueue.poll());
        }
    }

    public Task getRunningTask() {
        return runningTask;
    }

    public boolean isBusy() {
        if (isWorking()) {
            return runningTask.timeElapsed() >= (long) MeowLib.settings().get(MLibSettings.ASYNC_WORKER_BUSY).getValue();
        }
        return false;
    }



    public void queue(@NotNull final Task task) {
        taskQueue.add(task);
    }

    public boolean isWorking() {
        return getRunningTask() != null;
    }

    public boolean isFree() {
        return !isWorking() && queueSize() == 0;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void stopWorker() {
        running = false;
    }

    public void startWorker() {
        running = true;
        start();
    }
}
