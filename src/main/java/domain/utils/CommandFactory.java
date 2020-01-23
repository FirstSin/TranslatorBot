package domain.utils;

import domain.model.CommandType;
import domain.commands.Command;
import domain.commands.HelpCommand;
import domain.commands.StartCommand;

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
            default:
                throw new AssertionError("The passed command type was not found");
        }
        return command;
    }
}
