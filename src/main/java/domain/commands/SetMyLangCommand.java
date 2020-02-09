package domain.commands;

import dao.exceptions.DAOException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

public class SetMyLangCommand implements Command {
    private static final CommandType type = CommandType.SETMYLANG;
    private CommandExecutor executor;

    public SetMyLangCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(User user, String argument, SendMessage response) throws DAOException {
        executor.setMyLang(user, argument, response, this);
    }

    @Override
    public String toString() {
        return type.getCommand();
    }
}
