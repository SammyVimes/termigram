package online.senya.termigram.terminal;

/**
 * Created by Semyon on 03.01.2017.
 */
public interface CommandListener {

    void commandOutput(final String text);

    void commandCompleted(final String cmd, final int result);

    void commandFailed(final Exception exp);

}
