package domain.model;

import domain.utils.Handler;
import domain.utils.CommandHandler;
import domain.utils.TextHandler;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TranslatorBot extends TelegramLongPollingBot {

    private static final Logger logger = Logger.getLogger(TranslatorBot.class);
    private Handler commandHandler;
    private String username;
    private String token;
    private static TranslatorBot translatorBot;

    private TranslatorBot() {
        setBotProperties();
        commandHandler = new CommandHandler();
    }

    public static TranslatorBot getInstance(){
        if(translatorBot == null)
            translatorBot = new TranslatorBot();
        return translatorBot;
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
    }

    @Override
    public void onUpdateReceived(Update update) {
        boolean isNonNull = update.getMessage().getText() != null;
        if (update.hasMessage() && isNonNull) {
            BotApiMethod answer;
            boolean isCommand = update.getMessage().getText().startsWith("/");

            if (isCommand) {
                answer = commandHandler.handle(update);
            } else {
                answer = new TextHandler().handle(update);
            }

            try {
                execute(answer);
            } catch (TelegramApiException e) {
                logger.error("Problems with execution:\n" + e);
            }
        }

        //TODO: send translate message [22/01/20]
        //TODO: make a non-command text handler [23/01/20]
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
