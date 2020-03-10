package domain.handlers;

import dao.exceptions.DAOException;
import domain.commands.Command;
import domain.commands.CommandType;
import domain.exceptions.CommandNotFoundException;
import domain.utils.StatisticsCollector;
import domain.utils.CommandFactory;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandHandler implements Handler {

    private static final Logger logger = Logger.getLogger(CommandHandler.class);
    private CommandFactory commandFactory;

    public CommandHandler() {
        commandFactory = new CommandFactory();
    }

    @Override
    public void handle(Update update, SendMessage response) {
        logger.trace("Command processing starts. " + update.toString());
        Message message = update.getMessage();
        long chatId = message.getChatId();
        String text = message.getText();
        response.setChatId(chatId).setParseMode("HTML");
        CommandType type;
        try {
            type = defineCommandType(text);
        } catch (CommandNotFoundException e) {
            logger.error("An error occurred while defining the command", e);
            return;
        }
        Command command = commandFactory.getCommand(type);
        String argument = getCommandArgument(text);
        try {
            command.execute(message.getFrom(), argument, response);
        } catch (DAOException e) {
            logger.error("An error occurred in the DAO layer", e);
        }
        logger.debug(String.format("%s command executed successfully. Response: %S", command.toString(), response.toString()));
        StatisticsCollector.commandIncrement(command);
    }

    private CommandType defineCommandType(String message) throws CommandNotFoundException {
        String[] textValues = message.split(" ");
        String command = textValues[0].replaceAll("/", "");
        CommandType[] types = CommandType.values();
        for (CommandType type : types) {
            if (type.getCommandName().equalsIgnoreCase(command)) {
                logger.debug("Received command: " + command);
                return type;
            }
        }
        throw new CommandNotFoundException("The сommand '" + command + "' not found");
    }

    private String getCommandArgument(String message) {
        String[] values = message.split(" ");
        if (values.length < 2)
            return null;
        String arg = values[1];
        logger.debug("Command argument: " + arg);
        return arg;
    }
}
