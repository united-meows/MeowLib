package pisi.unitedmeows.meowlib.async;

public abstract class Task<X> {

    private State state = State.IDLE;
    protected Result<X> result;

    public abstract void run();

    public void pre() {
        state = State.RUNNING;
    }

    public void post() {
        state = State.FINISHED;
    }

    public Result result() {
        return result;
    }

    public enum State {
        FINISHED,
        RUNNING,
        ERROR,
        IDLE
    }
}
