package domain.commands;

import dao.exceptions.DAOException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

public class LangInfoCommand implements Command {
    private static final CommandType type = CommandType.LANGINFO;
    private CommandExecutor executor;

    public LangInfoCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(User user, String argument, SendMessage response) throws DAOException {
        executor.langInfo(user, response);
    }

    @Override
    public String toString() {
        return type.getCommandName();
    }
}
