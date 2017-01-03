package online.senya.termigram.threading;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Semyon on 03.01.2017.
 */
public class Looper {

    private Executor executor = Executors.newSingleThreadExecutor();

    public void addRunnable(final Runnable runnable) {
        executor.execute(runnable);
    }

}
