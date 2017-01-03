package online.senya.termigram;

import online.senya.termigram.terminal.Terminal;
import online.senya.termigram.terminal.TerminalCommand;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import javax.annotation.Nonnull;

/**
 * Created by Semyon on 03.01.2017.
 */
public class TermigramBot extends TelegramLongPollingBot {

    @Nonnull
    private Terminal terminal;

    private String token;

    private String botName;

    public TermigramBot(@Nonnull final Terminal terminal, final String token, final String botName) {
        this.terminal = terminal;
        this.token = token;
        this.botName = botName;
    }

    @Override
    public void onUpdateReceived(final Update update) {
        final Message message = update.getMessage();
        final String text = message.getText();
        terminal.send(new TerminalCommand(text));
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

}
