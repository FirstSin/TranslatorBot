package domain.utils;

import dao.exceptions.DAOException;
import domain.commands.Command;
import domain.exceptions.CommandNotFoundException;
import domain.commands.CommandType;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.validation.constraints.NotNull;

public class CommandHandler implements Handler {

    private static final Logger logger = Logger.getLogger(CommandHandler.class);
    private CommandFactory commandFactory;
    private static Handler handler;

    private CommandHandler() {
        commandFactory = new CommandFactory();
    }

    public static Handler getInstance() {
        if (handler == null)
            handler = new CommandHandler();
        return handler;
    }

    @Override
    public BotApiMethod handle(Update update) {
        Message message = update.getMessage();
        long chatId = message.getChatId();
        String text = message.getText();
        CommandType type = null;
        try {
            type = defineCommandType(text);
        } catch (CommandNotFoundException e) {
            logger.error("An error occurred while defining the command: " + e + " User message: " + text);
        }
        Command command = commandFactory.getCommand(type);
        String[] args = getCommandArgs(text);
        String commandAnswer = null;
        try {
            commandAnswer = command.execute(message, args);
        } catch (DAOException e) {
            logger.error("An error occurred in the DAO layer", e);
        }
        SendMessage response = new SendMessage();
        setResponseParams(response, chatId, commandAnswer);
        return response;
    }

    private CommandType defineCommandType(String message) throws CommandNotFoundException {
        String[] textValues = message.split(" ");
        String command = textValues[0].replaceAll("/", "");
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
        String arguments = message.substring(message.indexOf(" ") + 1);
        String[] args = arguments.split(" ");
        return args;
    }

    private void setResponseParams(@NotNull SendMessage response, @NotNull long chatId, @NotNull String text) {
        response.setChatId(chatId);
        response.setText(text);
        response.setParseMode("HTML");
    }
}
