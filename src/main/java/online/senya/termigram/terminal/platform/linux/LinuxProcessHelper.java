package online.senya.termigram.terminal.platform.linux;

import online.senya.termigram.terminal.TerminalCommand;
import online.senya.termigram.terminal.TerminalUtils;
import online.senya.termigram.terminal.platform.ProcessHelper;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Semyon on 03.01.2017.
 */
public class LinuxProcessHelper implements ProcessHelper {

    private File askpassScript;

    public LinuxProcessHelper() {
        try {
            this.askpassScript = createAskpassScript();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public Map<String, String> getEnvParams() {
        Map<String, String> envMap = new HashMap<>();
        envMap.put("DISPLAY", ":0");
        envMap.put("SSH_ASKPASS", askpassScript.getPath());
        envMap.put("SUDO_ASKPASS", askpassScript.getPath());
        return envMap;
    }

    private File createAskpassScript() throws IOException {
        final String suffix;
        final String script;
        // TODO: assuming that ssh-add runs the script in shell, not cmd
        suffix = ".sh";
        script = "#!/bin/sh\nread -p \"Enter password:\" pass\necho $pass\n";
        // for cmd following should work
        // suffix = ".bat";
        // script = "@echo %SSH_PASSPHRASE%\n";

        File askpass = File.createTempFile("askpass_", suffix);
        FileWriter askpassWriter = new FileWriter(askpass);
        askpassWriter.write(script);
        askpassWriter.close();

        // executable only for a current user
        askpass.setExecutable(false, false);
        askpass.setExecutable(true, true);
        return askpass;
    }

}
