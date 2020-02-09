package domain.commands;

import dao.exceptions.DAOException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

public class StartCommand implements Command {
    private static final CommandType type = CommandType.START;
    private CommandExecutor executor;

    public StartCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(User user, String argument, SendMessage response) throws DAOException {
        executor.start(user, response);
    }

    @Override
    public String toString() {
        return type.getCommandName();
    }
}
