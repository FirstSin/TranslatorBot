package domain.utils;

import domain.commands.Command;
import org.apache.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;

public class ArgumentsWaiter {
    private static final Logger logger = Logger.getLogger(ArgumentsWaiter.class);
    private Deque<Command> waitingCommand;
    private static ArgumentsWaiter argumentsWaiter;

    public static ArgumentsWaiter getInstance() {
        logger.trace("Getting an instance of a class");
        if (argumentsWaiter == null) {
            argumentsWaiter = new ArgumentsWaiter();
        }
        return argumentsWaiter;
    }

    private ArgumentsWaiter(){
        logger.info("Creating an instance of the bot class");
        waitingCommand = new ArrayDeque<>();
    }

    public boolean isWaiting() {
        return waitingCommand.isEmpty() ? false : true;
    }

    public void waitForArgs(Command command) {
        if(waitingCommand.size() >= 32) {
            logger.debug("Clearing the queue of pending commands");
            waitingCommand.clear();
        }
        waitingCommand.addLast(command);
    }

    public Command getWaitingCommand() {
        return waitingCommand.removeLast();
    }
}
