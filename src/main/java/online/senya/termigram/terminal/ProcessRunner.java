package online.senya.termigram.terminal;

import online.senya.termigram.terminal.platform.ProcessHelper;
import online.senya.termigram.threading.Handler;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Semyon on 03.01.2017.
 */
public class ProcessRunner extends Thread {

    private ProcessHelper processHelper;
    private final TerminalCommand terminalCommand;
    private final CommandListener listener;
    private final Handler handler;

    @Nullable
    private Process process;

    public ProcessRunner(final CommandListener listener, final TerminalCommand terminalCommand, final ProcessHelper processHelper, final Handler handler) {
        this.terminalCommand = terminalCommand;
        this.listener = listener;
        this.handler = handler;
        this.processHelper = processHelper;
    }

    @Override
    public void run() {
        final List<String> cmds = processHelper.commandToText(terminalCommand);
        if (cmds.isEmpty()) {
            listener.commandCompleted("", 0);
            return;
        }
        try {
            final ProcessBuilder pb = new ProcessBuilder(cmds);
            pb.redirectErrorStream(true);
            pb.environment();
            processHelper.getEnvParams().entrySet().forEach(stringStringEntry -> {
                pb.environment().put(stringStringEntry.getKey(), stringStringEntry.getValue());
            });

            final Process process = pb.start();

            handler.post(() -> this.process = process);

            final TerminalStreamReader reader = new TerminalStreamReader(listener, processHelper, process.getInputStream());
            // Need a stream writer...

            final int result = process.waitFor();

            // Terminate the stream writer
            reader.join();

            final StringJoiner sj = new StringJoiner(" ");
            cmds.forEach(sj::add);

            listener.commandCompleted(sj.toString(), result);
        } catch (final Exception exp) {
            exp.printStackTrace();
            listener.commandFailed(exp);
        }
    }

    @Nullable
    public Process getProcess() {
        return process;
    }

    public void write(final String text) throws IOException {
        String cmd = text + "\n";
        if (process != null && process.isAlive()) {
            process.getOutputStream().write(cmd.getBytes());
            process.getOutputStream().flush();
        }
    }
}