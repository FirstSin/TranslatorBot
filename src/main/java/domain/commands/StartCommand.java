package domain.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StartCommand extends BotCommand {

    public StartCommand(String command, String description) {
        super(command, description);
    }

    public void execute() {
        //TODO: Add command body [23/01/20]
    }
}
