package domain.model;

import domain.handlers.Handler;
import domain.handlers.MessageHandler;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TranslatorBot extends TelegramLongPollingBot {
    private static final Logger logger = Logger.getLogger(TranslatorBot.class);
    private String username;
    private String token;
    private static TranslatorBot translatorBot;

    private TranslatorBot() {
        setProperties();
    }

    public static TranslatorBot getInstance() {
        if (translatorBot == null) {
            translatorBot = new TranslatorBot();
            logger.info("Bot instance created");
        }
        return translatorBot;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Handler handler = new MessageHandler();
        SendMessage response = new SendMessage();

        handler.handle(update, response);
        sendMessage(response);
    }

    private void sendMessage(SendMessage message) {
        logger.trace("Sending message " + message);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Problems with message sending", e);
        }
        logger.trace("The message was successfully sent!");
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }


    private void setProperties() {
        try (InputStream in = new FileInputStream("src/main/resources/botinfo.properties")) {
            Properties prop = new Properties();
            prop.load(in);
            username = prop.getProperty("username");
            token = prop.getProperty("token");
        } catch (IOException e) {
            logger.error("An error occurred while loading properties: " + e.getMessage(), e);
        }
        logger.info("Bot properties set successfully");
    }
}
