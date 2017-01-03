package online.senya.termigram;

import online.senya.termigram.terminal.CommandListener;
import online.senya.termigram.terminal.Terminal;
import online.senya.termigram.terminal.TerminalCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by Semyon on 03.01.2017.
 */
public class ConsoleEntry {

    private static Logger LOGGER = LogManager.getLogger(ConsoleEntry.class);

    public static void main(String[] args) throws IOException {
        final Terminal terminal = new Terminal();
        terminal.addCommandListener(new CommandListener() {
            @Override
            public void commandOutput(final String text) {
                LOGGER.info(text);
            }

            @Override
            public void commandCompleted(final String cmd, final int result) {
                LOGGER.info(cmd + " [ " + result + " ]");
            }

            @Override
            public void commandFailed(final Exception exp) {
                LOGGER.error(exp);
            }

        });
        terminal.send(new TerminalCommand("ipconfig"));
        while (true) {
            String nextLine = "echo 228";
            terminal.send(new TerminalCommand(nextLine));
        }
    }

}
