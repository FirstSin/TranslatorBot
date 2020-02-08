package domain.utils;

import domain.handlers.CommandHandler;
import domain.handlers.Handler;
import domain.handlers.TextHandler;

public class HandlerSelector {
    public static Handler selectByMessage(String message) {
        boolean isCommand = message.startsWith("/");

        if (isCommand) {
            return CommandHandler.getInstance();
        } else {
            return TextHandler.getInstance();
        }
    }
}
