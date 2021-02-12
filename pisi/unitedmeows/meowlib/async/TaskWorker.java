package pisi.unitedmeows.meowlib.async;

import pisi.unitedmeows.meowlib.thread.kThread;

import java.util.List;

public class TaskWorker extends Thread{

    private List<Task<?>> taskList;
    private boolean running;

    public TaskWorker(List<Task<?>> tasks) {
        taskList = tasks;
    }

    @Override
    public void run() {
        while (running) {

            if (taskList.isEmpty()) {
                kThread.sleep(/* mlibsetting.async_wait_delay */);
                return;
            }


        }
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
