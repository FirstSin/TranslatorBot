package domain.handlers;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.commands.Command;
import domain.model.BotUser;
import domain.model.Translator;
import domain.model.YandexTranslator;
import domain.utils.ArgumentsWaiter;
import domain.utils.LocalizationUtils;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

public class TextHandler implements Handler {
    private static final Logger logger = Logger.getLogger(TextHandler.class);
    private static Handler textHandler;
    private Translator translator;
    private BotUserService userService = new BotUserService();
    private ArgumentsWaiter argumentsWaiter = ArgumentsWaiter.getInstance();

    private TextHandler() {
        translator = new YandexTranslator();
    }

    public static Handler getInstance() {
        if (textHandler == null)
            textHandler = new TextHandler();
        return textHandler;
    }

    @Override
    public void handle(Update update, SendMessage response) {
        Message message = update.getMessage();
        long chatId = message.getChatId();
        String text = message.getText();
        if (argumentsWaiter.isWaiting()) {
            redirectToCommand(update, response);
        } else {
            String translatedText = null;
            try {
                translatedText = translator.translate(text, getTranslationLang(update));
            } catch (IOException e) {
                logger.error("An error occurred while translating the text", e);
            } catch (DAOException e) {
                logger.error("An error occurred in the DAO layer", e);
            }

            response.setChatId(chatId).setText(translatedText).setParseMode("HTML");
        }
    }

    private String getTranslationLang(Update update) throws DAOException {
        BotUser botUser = userService.findUser(update.getMessage().getFrom().getId());
        return botUser.getTranslationLang();
    }

    private void redirectToCommand(Update update, SendMessage response){
        Command command = argumentsWaiter.getWaitingCommand();
        String argument = LocalizationUtils.getLangCode(update.getMessage().getText());
        try {
            command.execute(update.getMessage().getFrom(), argument, response);
            response.setChatId(update.getMessage().getChatId());
        } catch (DAOException e) {
            logger.error("An error occurred in the DAO layer", e);
        }
    }
}
