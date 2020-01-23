package domain.commands;

import domain.model.CommandType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class HelpCommand implements Command {
    private CommandType type = CommandType.HELP;

    public HelpCommand() { }

    public SendMessage execute(String[] args) {
        CommandType[] commands = CommandType.values();
        StringBuilder answer = new StringBuilder();
        answer.append("<b>Available commands:</b>\n\n");
        for (CommandType type : commands) {
            answer.append("/" + type.getCommand() + " - " + type.getDescription() + "\n");
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(answer.toString());
        sendMessage.setParseMode("HTML");
        return sendMessage;
    }

    public String getCommand() {
        return type.getCommand();
    }

    public String getDescription() {
        return type.getDescription();
    }
}
