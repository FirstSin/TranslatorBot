package domain.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StartCommand extends BotCommand {

    public StartCommand(String command, String description) {
        super(command, description);
    }

    void execute(AbsSender sender, User user, Chat chat, String[] args) {
        logger.info("User " + user.getId() + " is trying to execute command '" + getCommand() + "' the first time");
        StringBuilder answer = new StringBuilder();
        SendMessage message = new SendMessage();

        message.setChatId(chat.getId().toString());
        answer.append("Hi, " + user.getFirstName() + " " + user.getLastName() + "! You've just started this bot!");
        message.setText(answer.toString());
        execute(sender, message, user);
    }
}
