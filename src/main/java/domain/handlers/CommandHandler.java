package domain.handlers;

import dao.exceptions.DAOException;
import domain.commands.*;
import domain.exceptions.CommandNotFoundException;
import domain.utils.StatisticsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    private static Map<String, Command> availableCommands;
    private Command currentCommand;

    static {
        initCommands();
    }

    @Override
    public void handle(Update update, SendMessage response) {
        logger.trace("Command processing starts");
        Message message = update.getMessage();
        long chatId = message.getChatId();
        String text = message.getText();

        response.setChatId(chatId).setParseMode("HTML");

        try {
            currentCommand = availableCommands.get(defineCommand(text));
            currentCommand.execute(message.getFrom(), needArgument() ? getCommandArgument(text) : null, response);
        } catch (CommandNotFoundException e) {
            logger.error("An error occurred while defining the command", e);
            return;
        } catch (DAOException e) {
            logger.error("An error occurred in the DAO layer", e);
            return;
        }

        logger.trace("{} command executed successfully", currentCommand);
        StatisticsCollector.incrementCommandCount(currentCommand);
    }

    private String defineCommand(String message) throws CommandNotFoundException {
        String command = message.split(" ")[0].substring(1);
        if (availableCommands.containsKey(command)) {
            logger.debug("Command: {}", command);
            return command;
        }
        throw new CommandNotFoundException("The command '" + message + "' not found");
    }

    private String getCommandArgument(String message) {
        String[] values = message.split(" ");

        if (values.length < 2) return null;

        String arg = values[1];
        logger.debug("Command argument: '{}' ", arg);
        return arg;
    }

    private boolean needArgument() {
        return String.valueOf(currentCommand).equals(CommandType.SETMYLANG.toString())
                || String.valueOf(currentCommand).equals(CommandType.TOLANG.toString());
    }

    private static void initCommands(){
        availableCommands = new HashMap<>();
        availableCommands.put("start", new StartCommand());
        availableCommands.put("help", new HelpCommand());
        availableCommands.put("langinfo", new LangInfoCommand());
        availableCommands.put("setmylang", new SetMyLangCommand());
        availableCommands.put("tolang", new ToLangCommand());
        availableCommands.put("stat", new StatCommand());
    }
}
