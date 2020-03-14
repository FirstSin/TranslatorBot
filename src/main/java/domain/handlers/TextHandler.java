package domain.handlers;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.commands.Command;
import domain.model.BotUser;
import domain.model.Translator;
import domain.model.YandexTranslator;
import domain.utils.ArgumentRequester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.StringJoiner;

public class TextHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(TextHandler.class);
    private Translator translator;
    private BotUserService userService = new BotUserService();
    private ArgumentRequester argumentRequester = ArgumentRequester.getInstance();

    public TextHandler() {
        translator = YandexTranslator.getInstance();
    }

    @Override
    public void handle(Update update, SendMessage response) {
        logger.debug("Text processing starts. {}", update.toString());
        int userId = update.getMessage().getFrom().getId();
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        response.setChatId(chatId).setParseMode("HTML");

        if (argumentRequester.isRequested(userId)) {
            redirectToCommand(update, response);
        } else {
            try {
                String translatedText = translator.translate(text, getTranslationLang(update));
                if(translatedText != null) response.setText(translatedText);
            } catch (IOException e) {
                logger.error("An error occurred while translating the text", e);
            } catch (DAOException e) {
                logger.error("An error occurred in the DAO layer", e);
            }
        }
        logger.debug("Text processed successfully. Response: {}", response.toString());
    }

    private String getTranslationLang(Update update) throws DAOException {
        BotUser botUser = userService.findUser(update.getMessage().getFrom().getId());
        return botUser.getTranslationLang();
    }

    private void redirectToCommand(Update update, SendMessage response) {
        Command command = argumentRequester.getRequestedCommand(update.getMessage().getFrom().getId());
        String argument = update.getMessage().getText();
        try {
            command.execute(update.getMessage().getFrom(), argument, response);
            response.setChatId(update.getMessage().getChatId());
        } catch (DAOException e) {
            logger.error("An error occurred in the DAO layer", e);
        }
        logger.debug("The argument '{}' was redirected to the {} command", argument, command.toString());
    }
}
