package online.senya.termigram.terminal.platform;

import online.senya.termigram.terminal.TerminalCommand;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * Created by Semyon on 03.01.2017.
 */
public interface ProcessHelper {

    int getPid(@Nonnull final Process process);

    void cancelProcess(@Nonnull Process process, int pid);

    String decode(final byte[] buffer, final int offset, final int length);

    List<String> commandToText(TerminalCommand terminalCommand);

    Map<String, String> getEnvParams();
}
