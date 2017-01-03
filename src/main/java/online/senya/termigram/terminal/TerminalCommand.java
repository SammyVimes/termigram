package online.senya.termigram.terminal;

/**
 * Created by Semyon on 03.01.2017.
 */
public class TerminalCommand {

    private final String text;

    public TerminalCommand(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
