package domain.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

public class StatCommand implements Command {
    private static CommandType type = CommandType.TOLANG;
    private CommandExecutor executor;

    public StatCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(User user, String argument, SendMessage response) {
        executor.stat(response);
    }

    @Override
    public String toString() {
        return type.getCommandName();
    }
}
