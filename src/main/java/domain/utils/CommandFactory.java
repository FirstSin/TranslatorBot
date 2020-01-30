package domain.utils;

import domain.commands.*;
import domain.commands.CommandType;

import javax.validation.constraints.NotNull;

public class CommandFactory {
    public Command getCommand(@NotNull CommandType type){
        Command command = null;
        switch (type){
            case START:
                command = new StartCommand();
                break;
            case HELP:
                command = new HelpCommand();
                break;
            case LANGINFO:
                command = new LangInfoCommand();
                break;
            case SETMYLANG:
                command = new SetMyLangCommand();
                break;
            case TOLANG:
                command = new ToLangCommand();
                break;
            default:
                throw new AssertionError("The passed command type was not found");
        }
        return command;
    }
}
