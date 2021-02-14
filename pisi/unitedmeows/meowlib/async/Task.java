package pisi.unitedmeows.meowlib.async;

public abstract class Task<X> {

    private State state = State.IDLE;
    protected Result<X> result;
    private long startTime;
    private IAsyncAction action;
    private Object assign;

    public Task(IAsyncAction action, Object assign) {
        this.action = action;
        this.assign = assign;
    }

    public void pre() {
        state = State.RUNNING;
        startTime = runningTime();
    }

    public void run() {

    }


    public void post() {
        state = State.FINISHED;
    }


    public Object getAssign() {
        return assign;
    }

    public long runningTime() {
        return System.nanoTime() / 1000000L;
    }

    public long timeElapsed() {
        return runningTime() - startTime;
    }

    public State state() {
        return state;
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
