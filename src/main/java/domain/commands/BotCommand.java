package domain.commands;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

abstract class BotCommand {

    protected final Logger logger = Logger.getLogger(BotCommand.class);

    private String command;
    private String description;

    public BotCommand(String command, String description) {
        this.command = command;
        this.description = description;
    }

    void execute(AbsSender sender, SendMessage message, User user) {
        try {
            sender.execute(message);
            logger.info("SUCCESS: " + user.getId() + ", " + getCommand());
        } catch (TelegramApiException e) {
            logger.error("Command exception: " + user.getId() + ", " + getCommand(), e);
        }
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}
