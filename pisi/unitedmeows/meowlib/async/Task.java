package pisi.unitedmeows.meowlib.async;

public abstract class Task<X> {

    private State state = State.IDLE;
    protected Result<X> result;
    private long startTime;

    public abstract void run();

    public void pre() {
        state = State.RUNNING;
        startTime = System.currentTimeMillis();
    }

    public void post() {
        state = State.FINISHED;
    }


    public long runningTime() {
        return System.nanoTime() / 1000000L;
    }

    public long timeElapsed() {
        return runningTime() - startTime;
    }


    public Result<X> result() {
        return result;
    }

    public enum State {
        FINISHED,
        RUNNING,
        ERROR,
        IDLE
    }
}
