package online.senya.termigram;

import online.senya.termigram.terminal.CommandListener;
import online.senya.termigram.terminal.Terminal;
import online.senya.termigram.terminal.TerminalCommand;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Semyon on 03.01.2017.
 */
public class TermigramBot extends TelegramLongPollingBot {

    @Nonnull
    private final Terminal terminal;

    private final String token;

    private final String botName;

    private final int ownerId;

    public TermigramBot(@Nonnull final Terminal terminal, final String token, final String botName, final int ownerId) {
        this.terminal = terminal;
        this.token = token;
        this.botName = botName;
        this.ownerId = ownerId;
    }

    @Override
    public void onUpdateReceived(final Update update) {
        final Message message = update.getMessage();
        User from = message.getFrom();
        Long chatId = message.getChatId();

        if (!from.getId().equals(ownerId)) {
            try {
                SendMessage msg = new SendMessage();
                msg.setText("Мужчина, вы что не видите? У нас обед");
                msg.setChatId(chatId);
                sendMessage(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }

        final String text = message.getText();

        switch (text) {
            case "Ctrl+C":
                terminal.cancel();
                return;
            case "Ctrl+D":
                terminal.send(new TerminalCommand("exit"));
                return;
        }

        terminal.send(new TerminalCommand(text), new CommandListener() {
            @Override
            public void commandOutput(String text) {
                try {
                    SendMessage msg = new SendMessage();
                    msg.setText(text);
                    msg.setChatId(chatId);
                    sendMessage(msg);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void commandCompleted(String cmd, int result) {

            }

            @Override
            public void commandFailed(Exception exp) {

            }
        });
        sendMessageQuietly(createMessageWithKeyboard(chatId, getRunningProcessKeyboard()).setText("."));
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    private ReplyKeyboardMarkup getRunningProcessKeyboard() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Ctrl+C"));
        row.add(new KeyboardButton("Ctrl+D"));
        keyboard.add(row);
        row = new KeyboardRow();
        row.add(new KeyboardButton("Show passboard"));
        keyboard.add(row);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private SendMessage createMessageWithKeyboard(final Long chatId, final ReplyKeyboardMarkup replyKeyboardMarkup) {

        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);

        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }

        return sendMessage;
    }

    private void sendMessageQuietly(final SendMessage msg) {
        try {
            sendMessage(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
