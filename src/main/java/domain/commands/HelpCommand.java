package domain.commands;

import domain.model.CommandType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class HelpCommand implements Command {
    private CommandType type = CommandType.HELP;

    public HelpCommand() { }

    public String execute(Message message, String[] args) {
        CommandType[] commands = CommandType.values();
        StringBuilder answer = new StringBuilder();
        answer.append("<b>Available commands:</b>\n");
        for (CommandType type : commands) {
            answer.append("/" + type.getCommand() + " - " + type.getDescription() + "\n");
        }
        return answer.toString();
    }

    public String getCommand() {
        return type.getCommand();
    }

    public String getDescription() {
        return type.getDescription();
    }
}
