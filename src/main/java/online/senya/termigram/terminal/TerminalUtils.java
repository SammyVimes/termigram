package online.senya.termigram.terminal;

import online.senya.termigram.terminal.platform.ProcessHelper;
import online.senya.termigram.terminal.platform.linux.LinuxProcessHelper;
import online.senya.termigram.terminal.platform.windows.WindowsProcessHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Semyon on 03.01.2017.
 */
public class TerminalUtils {

    public static List<String> commandToText(final TerminalCommand terminalCommand) {
        String cmd = terminalCommand.getText();
        if (cmd == null) {
            return Collections.emptyList();
        }
        if (!cmd.trim().isEmpty()) {

            final List<String> values = new ArrayList<>(10);
            if (cmd.contains("\"")) {

                while (cmd.contains("\"")) {

                    final String start = cmd.substring(0, cmd.indexOf("\""));
                    cmd = cmd.substring(start.length());
                    String quote = cmd.substring(cmd.indexOf("\"") + 1);
                    cmd = cmd.substring(cmd.indexOf("\"") + 1);
                    quote = quote.substring(0, cmd.indexOf("\""));
                    cmd = cmd.substring(cmd.indexOf("\"") + 1);

                    if (!start.trim().isEmpty()) {
                        final String[] parts = start.trim().split(" ");
                        values.addAll(Arrays.asList(parts));
                    }
                    values.add(quote.trim());

                }

                if (!cmd.trim().isEmpty()) {
                    final String[] parts = cmd.trim().split(" ");
                    values.addAll(Arrays.asList(parts));
                }

                for (final String value : values) {
                    System.out.println("[" + value + "]");
                }

            } else {

                if (!cmd.trim().isEmpty()) {
                    final String[] parts = cmd.trim().split(" ");
                    values.addAll(Arrays.asList(parts));
                }

            }
            return values;
        }
        return Collections.emptyList();
    }

    public static ProcessHelper getProcessHelper() {
        final String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return new WindowsProcessHelper();
        } else if ((osName.contains("nix") || osName.contains("nux") || osName.indexOf("aix") > 0 )) {
            return new LinuxProcessHelper();
        } else {
            throw new RuntimeException("Only windows and linux are supported at this moment");
        }
    }

}
