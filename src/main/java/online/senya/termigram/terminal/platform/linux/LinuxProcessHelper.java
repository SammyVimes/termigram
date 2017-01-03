package online.senya.termigram.terminal.platform.linux;

import online.senya.termigram.terminal.TerminalCommand;
import online.senya.termigram.terminal.TerminalUtils;
import online.senya.termigram.terminal.platform.ProcessHelper;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Semyon on 03.01.2017.
 */
public class LinuxProcessHelper implements ProcessHelper {

    @Override
    public int getPid(@Nonnull final Process process) {
        try {
            final Class<?> cProcessImpl = process.getClass();
            final Field fPid = cProcessImpl.getDeclaredField("pid");
            if (!fPid.isAccessible()) {
                fPid.setAccessible(true);
            }
            return fPid.getInt(process);
        } catch (final Exception e) {
            return -1;
        }
    }

    @Override
    public void cancelProcess(@Nonnull final Process process, final int pid) {
        if (pid != -1) {
            try {
                Runtime.getRuntime().exec("kill -SIGINT " + pid).waitFor();
                return;
            } catch (InterruptedException | IOException e) {
                // failed to kill process, do it with java
            }
        }
        if (process.isAlive()) {
            process.destroyForcibly();
        }
    }

    @Override
    public String decode(final byte[] buffer, final int offset, final int length) {
        return new String(buffer, offset, length);
    }

    @Override
    public List<String> commandToText(final TerminalCommand terminalCommand) {
        return TerminalUtils.commandToText(terminalCommand);
    }

}
