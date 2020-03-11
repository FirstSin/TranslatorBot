package domain.handlers;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageHandler implements Handler {
    private static final Logger logger = Logger.getLogger(MessageHandler.class);

    @Override
    public void handle(Update update, SendMessage response) {
        logger.trace("Starting processing message...");
        String text = update.getMessage().getText();
        boolean isNonNull = text != null;

        if (update.hasMessage() && isNonNull) {
            Handler messageHandler;
            boolean isCommand = text.startsWith("/");

            if (isCommand) {
                messageHandler = new CommandHandler();
                logger.debug("Command handler was selected for the message: " + text);
            } else {
                messageHandler = new TextHandler();
                logger.debug("Text handler was selected for the message: " + text);
            }

            messageHandler.handle(update, response);
        }
        logger.trace("The message was successfully processed");
    }
}
