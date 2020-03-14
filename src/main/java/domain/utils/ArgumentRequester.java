package domain.utils;

import domain.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ArgumentRequester {
    private static final Logger logger = LoggerFactory.getLogger(ArgumentRequester.class);
    private Map<Integer, Command> waitingCommands;
    private static ArgumentRequester argumentRequester;

    public static ArgumentRequester getInstance() {
        if (argumentRequester == null) {
            argumentRequester = new ArgumentRequester();
            logger.info("Instance created");
        }
        return argumentRequester;
    }

    private ArgumentRequester() {
        waitingCommands = new HashMap<>();
    }

    public boolean isRequested(int userId) {
        return waitingCommands.containsKey(userId);
    }

    public void requestArgument(int userId, Command command) {
        waitingCommands.put(userId, command);
    }

    public Command getRequestedCommand(int userId) {
        Command command = waitingCommands.remove(userId);
        logger.debug("Getting {} command from the waiting commands collection", command.toString());
        return command;
    }

    public void deleteArgumentRequest(int userId) {
        waitingCommands.remove(userId);
    }
}
