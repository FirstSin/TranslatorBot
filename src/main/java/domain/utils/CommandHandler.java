package domain.utils;

import com.google.common.base.Joiner;
import domain.exceptions.CommandNotFoundException;
import domain.model.CommandType;
import domain.commands.Command;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandHandler implements Handler {

    private static final Logger logger = Logger.getLogger(CommandHandler.class);
    private CommandFactory factory = new CommandFactory();

    @Override
    public BotApiMethod handle(Update update) {
        int userId = update.getMessage().getFrom().getId();
        long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();
        CommandType type = null;
        try {
            type = defineCommandType(message);
        } catch (CommandNotFoundException e) {
            logger.error("An error occurred while defining the command: " + e + " User message: " + message);
        }
        Command command = factory.getCommand(type);
        String[] args = getCommandArgs(message);
        String commandAnswer = command.execute(args);
        SendMessage response = new SendMessage();
        response.setText(commandAnswer);
        response.setChatId(chatId);
        response.setParseMode("HTML");
        return response;
    }

    private CommandType defineCommandType(String message) throws CommandNotFoundException {
        String[] textValues = message.split(" ");
        String command = textValues[0].substring(1);
        CommandType[] types = CommandType.values();
        for (CommandType type : types) {
            if (type.toString().equalsIgnoreCase(command))
                return type;
        }
        throw new CommandNotFoundException("The сommand '" + command + "' not found.");
    }

    private String[] getCommandArgs(String message) {
        if (message.split(" ").length < 2)
            return null;
        String arguments = message.substring(message.indexOf(" " + 1));
        String[] args = arguments.split(" ");
        return args;
    }
}
