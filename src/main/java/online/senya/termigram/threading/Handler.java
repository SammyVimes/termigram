package online.senya.termigram.threading;

/**
 * Created by Semyon on 03.01.2017.
 */
public class Handler {

    private Looper looper;

    public Handler(final Looper looper) {
        this.looper = looper;
    }

    public void post(final Runnable runnable) {
        looper.addRunnable(runnable);
    }

}
