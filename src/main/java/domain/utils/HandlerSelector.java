package domain.utils;

import domain.handlers.CommandHandler;
import domain.handlers.Handler;
import domain.handlers.TextHandler;
import org.apache.log4j.Logger;

public class HandlerSelector {
    private static final Logger logger = Logger.getLogger(HandlerSelector.class);

    public static Handler selectByMessage(String message) {
        boolean isCommand = message.startsWith("/");
        if (isCommand) {
            logger.debug("A text handler was selected for the message " + message);
            return new CommandHandler();
        } else {
            logger.debug("A command handler was selected for the message " + message);
            return new TextHandler();
        }
    }
}
