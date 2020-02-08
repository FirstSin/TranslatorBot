package domain.utils;

import domain.commands.Command;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ArgumentsWaiter {
    private static final Logger logger = Logger.getLogger(ArgumentsWaiter.class);
    private Map<Integer, Command> waitingCommands;
    private static ArgumentsWaiter argumentsWaiter;

    public static ArgumentsWaiter getInstance() {
        if (argumentsWaiter == null) {
            argumentsWaiter = new ArgumentsWaiter();
            logger.info("Instance created");
        }
        return argumentsWaiter;
    }

    private ArgumentsWaiter(){
        waitingCommands = new HashMap<>();
    }

    public boolean isWaiting(int userId) {
        return waitingCommands.containsKey(userId);
    }

    public void waitForArgs(int userId, Command command) {
        waitingCommands.put(userId, command);
    }

    public Command getWaitingCommand(int userId) {
        Command command = waitingCommands.remove(userId);
        logger.debug("Getting " + command.toString() + " command from the waiting commands collection");
        return command;
    }
}
