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
    private Task runningTask;

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
            runningTask = taskQueue.poll();
            runningTask.pre();
            Task<?> result = runningTask.run();
            if (runningTask.getAssign() != null) {
               /* TODO: ^^._.^^ */
            }
            runningTask.post();
        }
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
        return !isWorking();
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
