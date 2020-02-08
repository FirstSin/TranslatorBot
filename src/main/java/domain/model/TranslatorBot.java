package domain.model;

import domain.handlers.Handler;
import domain.utils.HandlerSelector;
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
        setBotProperties();
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
        boolean isNonNull = update.getMessage().getText() != null;
        if (update.hasMessage() && isNonNull) {
            new Thread(() -> sendMsg(update)).start();
        }
    }

    public void sendMsg(Update update) {
        logger.trace("Start processing the message");
        SendMessage response = new SendMessage();
        Handler handler = HandlerSelector.selectByMessage(update.getMessage().getText());
        handler.handle(update, response);
        try {
            execute(response);
        } catch (TelegramApiException e) {
            logger.error("Problems with execution:\n" + e);
        }
        logger.trace("the message was successfully processed and sent");
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }


    private void setBotProperties() {
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
