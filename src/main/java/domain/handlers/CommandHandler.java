package domain.handlers;

import dao.exceptions.DAOException;
import domain.commands.Command;
import domain.commands.CommandType;
import domain.exceptions.CommandNotFoundException;
import domain.utils.CommandFactory;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

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
    public void handle(Update update, SendMessage response) {
        logger.trace("Processing of the command message begins");
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
        String argument = getCommandArgument(text);
        try {
            command.execute(update.getMessage().getFrom(), argument, response);
        } catch (DAOException e) {
            logger.error("An error occurred in the DAO layer", e);
        }

        response.setChatId(chatId).setParseMode("HTML");
        logger.trace("Command processing was successful");
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

    private String getCommandArgument(String message) {
        String[] values = message.split(" ");
        if (values.length < 2)
            return null;
        return values[1];
    }
}
