package online.senya.termigram.terminal;

import online.senya.termigram.terminal.platform.ProcessHelper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Semyon on 03.01.2017.
 */
class TerminalStreamReader extends Thread {

    private final InputStream is;
    private final CommandListener listener;
    private final ProcessHelper processHelper;

    TerminalStreamReader(final CommandListener listener, final ProcessHelper processHelper, final InputStream is) {
        this.is = is;
        this.listener = listener;
        this.processHelper = processHelper;
        start();
    }

    @Override
    public void run() {
        try {
            int length = -1;
            final byte[] buff = new byte[1024];
            while ((length = is.read(buff)) != -1) {
                final String output = processHelper.decode(buff, 0, length);
                listener.commandOutput(output);
            }
        } catch (final IOException exp) {
            exp.printStackTrace();
        }
    }
}

