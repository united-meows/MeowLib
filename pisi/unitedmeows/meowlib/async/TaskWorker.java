package pisi.unitedmeows.meowlib.async;

import pisi.unitedmeows.meowlib.MeowLib;
import pisi.unitedmeows.meowlib.etc.MLibSettings;

public class TaskWorker extends Thread {

    private boolean running;
    private Task<?> runningTask;
    private ITaskPool pool;
    private long lastWork;

    public TaskWorker(ITaskPool owner) {
        pool = owner;
        lastWork = curTime();
    }

    @Override
    public void run() {
        while (running) {
            try {
                runningTask = pool.poll();
                runningTask.pre();
                runningTask.run();
                runningTask.post();
                runningTask = null;
                lastWork =  curTime();
            } catch (NoSuchMethodError | NullPointerException ex) {
                runningTask = null;
            }
        }
    }


    private long curTime() {
        return System.nanoTime() / 1000000L;
    }

    public long lastWorkTimeElapsed() {
        return curTime() - lastWork;
    }


    public Task getRunningTask() {
        return runningTask;
    }

    public boolean isBusy() {
        if (isWorking()) {
            return runningTask.timeElapsed() >= (long) MeowLib.mLibSettings().get(MLibSettings.ASYNC_WORKER_BUSY).getValue();
        }
        return false;
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
