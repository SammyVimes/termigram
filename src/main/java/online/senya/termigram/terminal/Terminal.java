package online.senya.termigram.terminal;

import online.senya.termigram.terminal.platform.ProcessHelper;
import online.senya.termigram.threading.Handler;
import online.senya.termigram.threading.Looper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Semyon on 03.01.2017.
 */
public class Terminal implements CommandListener {

    private static Logger LOGGER = LogManager.getLogger(Terminal.class);

    @Nullable
    private UserCredentials userCredentials;

    private List<CommandListener> listeners = new ArrayList<>();

    @Nullable
    private ProcessRunner currentProcessRunner = null;

    private ProcessHelper processHelper;

    private Looper looper = new Looper();

    private Handler handler = new Handler(looper);

    public Terminal(@Nullable final UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
        init();
    }

    public Terminal() {
        init();
    }

    private void init() {
        this.processHelper = TerminalUtils.getProcessHelper();
    }

    public void send(final TerminalCommand command) {
        send(command, null);
    }

    public void send(final TerminalCommand command, final CommandListener commandListener) {
        handler.post(() -> {
            if (currentProcessRunner == null) {
                currentProcessRunner = new ProcessRunner(new CommandListener() {
                    @Override
                    public void commandOutput(String text) {
                        Terminal.this.commandOutput(text);
                        if (commandListener != null) {
                            commandListener.commandOutput(text);
                        }
                    }

                    @Override
                    public void commandCompleted(String cmd, int result) {
                        Terminal.this.commandCompleted(cmd, result);
                        if (commandListener != null) {
                            commandListener.commandCompleted(cmd, result);
                        }
                    }

                    @Override
                    public void commandFailed(Exception exp) {
                        Terminal.this.commandFailed(exp);
                        if (commandListener != null) {
                            commandListener.commandFailed(exp);
                        }
                    }
                }, command, processHelper, handler);
                currentProcessRunner.start();
            } else {
                try {
                    currentProcessRunner.write(command.getText());
                } catch (final IOException e) {
                    LOGGER.error(e);
                }
            }
        });
    }

    /**
     * It's Ctrl+C
     */
    public void cancel() {
        final ProcessRunner processRunner = currentProcessRunner;
        if (processRunner == null) {
            return;
        }
        handler.post(() -> {
            final Process process = processRunner.getProcess();
            if (process == null) {
                return;
            }
            final int pid = processHelper.getPid(process);
            processHelper.cancelProcess(process, pid);
        });
    }

    /**
     * Kill current running process
     */
    public void kill() {
        final ProcessRunner processRunner = currentProcessRunner;
        if (processRunner == null) {
            return;
        }
        handler.post(() -> {
            final Process process = processRunner.getProcess();
            if (process == null) {
                return;
            }
            process.destroyForcibly();
        });
    }

    @Override
    public void commandOutput(final String text) {
        for (final CommandListener listener : listeners) {
            listener.commandOutput(text);
        }
    }

    @Override
    public void commandCompleted(final String cmd, final int result) {
        for (final CommandListener listener : listeners) {
            listener.commandCompleted(cmd, result);
        }
        this.currentProcessRunner = null;
    }

    @Override
    public void commandFailed(final Exception exp) {
        for (final CommandListener listener : listeners) {
            listener.commandFailed(exp);
        }
        this.currentProcessRunner = null;
    }

    public void addCommandListener(final CommandListener listener) {
        listeners.add(listener);
    }

    public void removeCommandListener(final CommandListener listener) {
        listeners.remove(listener);
    }

}
