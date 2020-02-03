package domain.utils;

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
