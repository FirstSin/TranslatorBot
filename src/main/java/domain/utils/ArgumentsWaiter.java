package domain.utils;

import domain.commands.Command;
import org.apache.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;

public class ArgumentsWaiter {
    private static final Logger logger = Logger.getLogger(ArgumentsWaiter.class);
    private Deque<Command> waitingCommands;
    private static ArgumentsWaiter argumentsWaiter;

    public static ArgumentsWaiter getInstance() {
        if (argumentsWaiter == null) {
            argumentsWaiter = new ArgumentsWaiter();
            logger.info("Instance crated");
        }
        return argumentsWaiter;
    }

    private ArgumentsWaiter(){
        waitingCommands = new ArrayDeque<>();
    }

    public boolean isWaiting() {
        return waitingCommands.isEmpty() ? false : true;
    }

    public void waitForArgs(Command command) {
        logger.debug("Waiting commands queue size: " + waitingCommands.size());
        if(waitingCommands.size() >= 32) {
            logger.debug("Clearing the queue of pending commands");
            waitingCommands.clear();
        }
        waitingCommands.addLast(command);
    }

    public Command getWaitingCommand() {
        Command command = waitingCommands.removeLast();
        logger.debug("Removing " + command.toString() + " command from the queue");
        return command;
    }
}
