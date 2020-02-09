package domain.commands;

import dao.exceptions.DAOException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

public class HelpCommand implements Command {
    private CommandType type = CommandType.HELP;
    private CommandExecutor executor;

    public HelpCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(User user, String argument, SendMessage response) throws DAOException {
        executor.help(user, response);
    }

    @Override
    public String toString() {
        return type.getCommand();
    }
}
