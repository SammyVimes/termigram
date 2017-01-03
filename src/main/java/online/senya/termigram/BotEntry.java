package online.senya.termigram;

import online.senya.termigram.terminal.Terminal;
import org.apache.http.util.TextUtils;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Semyon on 03.01.2017.
 */
public class BotEntry {

    public static void main(final String[] args) {
        final Properties properties = new Properties();

        try {
            properties.load(properties.getClass().getResourceAsStream("/secret.properties"));
        } catch (IOException e) {
            throw new RuntimeException("No secret.properties with telegram token and bot name found in resources/");
        }

        String token = properties.getProperty("token");
        String botName = properties.getProperty("bot_name");

        if (TextUtils.isEmpty(token)) {
            throw new RuntimeException("No telegram token found in resources/secret.properties");
        }

        if (TextUtils.isEmpty(botName)) {
            throw new RuntimeException("No telegram bot_name found in resources/secret.properties");
        }

        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new TermigramBot(new Terminal(), token, botName));
        } catch (final TelegramApiException e) {
        }
    }

}
