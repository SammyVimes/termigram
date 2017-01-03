package online.senya.termigram.terminal.platform.windows;

import online.senya.termigram.terminal.TerminalCommand;
import online.senya.termigram.terminal.TerminalUtils;
import online.senya.termigram.terminal.platform.ProcessHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Semyon on 03.01.2017.
 */
public class WindowsProcessHelper implements ProcessHelper {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public int getPid(@Nonnull final Process process) {
        return 0;
    }

    @Override
    public void cancelProcess(@Nonnull final Process process, final int pid) {

        if (process.isAlive()) {
            process.destroyForcibly();
        }
    }

    @Override
    public String decode(final byte[] buffer, final int offset, final int length) {
        try {
            return new String(buffer, offset, length, "Cp866");
        } catch (final UnsupportedEncodingException e) {
            LOGGER.error(e);
            return "";
        }
    }

    @Override
    public List<String> commandToText(final TerminalCommand terminalCommand) {
        final List<String> cmds = TerminalUtils.commandToText(terminalCommand);
        //TODO: make this configurable
        cmds.add(0, "/c");
        cmds.add(0, "cmd.exe");
        return cmds;
    }

    @Override
    public Map<String, String> getEnvParams() {
        return Collections.emptyMap();
    }

}
