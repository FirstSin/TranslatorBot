package domain.handlers;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.commands.Command;
import domain.model.BotUser;
import domain.model.Translator;
import domain.model.YandexTranslator;
import domain.utils.ArgumentsWaiter;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.StringJoiner;

public class TextHandler implements Handler {
    private static final Logger logger = Logger.getLogger(TextHandler.class);
    private Translator translator;
    private BotUserService userService = new BotUserService();
    private ArgumentsWaiter argumentsWaiter = ArgumentsWaiter.getInstance();

    public TextHandler() {
        translator = YandexTranslator.getInstance();
    }

    @Override
    public void handle(Update update, SendMessage response) {
        logger.debug("Text processing starts. " + update.toString());
        int userId = update.getMessage().getFrom().getId();
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        if (argumentsWaiter.isWaiting(userId)) {
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
        logger.debug("Text processed successfully. Response: " + response.toString());
    }

    private String getTranslationLang(Update update) throws DAOException {
        BotUser botUser = userService.findUser(update.getMessage().getFrom().getId());
        return botUser.getTranslationLang();
    }

    private void redirectToCommand(Update update, SendMessage response) {
        Command command = argumentsWaiter.getWaitingCommand(update.getMessage().getFrom().getId());
        String argument = update.getMessage().getText();
        try {
            command.execute(update.getMessage().getFrom(), argument, response);
            response.setChatId(update.getMessage().getChatId());
        } catch (DAOException e) {
            logger.error("An error occurred in the DAO layer", e);
        }
        logger.debug(new StringJoiner(" ").add("The argument").add(argument)
                                          .add("was redirected to the").add(command.toString())
                                          .add("command").toString());
    }
}
