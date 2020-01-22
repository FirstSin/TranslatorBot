package domain;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TranslatorBot extends TelegramLongPollingBot {

    private static final Logger logger = Logger.getLogger(TranslatorBot.class);
    private static String username;
    private static String token;

    public TranslatorBot() {
        if (username == null || token == null) {
            try (InputStream in = new FileInputStream("src/main/resources/botinfo.properties")) {
                Properties prop = new Properties();
                prop.load(in);
                username = prop.getProperty("username");
                token = prop.getProperty("token");
            } catch (IOException e) {
                logger.error("An error occurred while loading properties", e);
            }
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        //TODO: send translate message [22/01/20]
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
