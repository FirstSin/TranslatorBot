package domain.utils;

import domain.commands.*;

import javax.validation.constraints.NotNull;

public class CommandFactory {
    private CommandExecutor commandExecutor = new CommandExecutor();

    public Command getCommand(@NotNull CommandType type) {
        Command command = null;
        switch (type) {
            case START:
                command = new StartCommand(commandExecutor);
                break;
            case HELP:
                command = new HelpCommand(commandExecutor);
                break;
            case LANGINFO:
                command = new LangInfoCommand(commandExecutor);
                break;
            case SETMYLANG:
                command = new SetMyLangCommand(commandExecutor);
                break;
            case TOLANG:
                command = new ToLangCommand(commandExecutor);
                break;
        }
        return command;
    }
}
