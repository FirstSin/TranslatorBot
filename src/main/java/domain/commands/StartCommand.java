package domain.commands;

import domain.model.CommandType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class StartCommand implements Command {
    private CommandType type = CommandType.START;

    public StartCommand() { }

    public String execute(String[] args) {
        return null;
    }

    public String getCommand() {
        return type.getCommand();
    }

    public String getDescription() {
        return type.getDescription();
    }
}
