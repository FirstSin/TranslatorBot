package domain.utils;

import domain.commands.*;
import domain.model.CommandType;

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
            case BOTLANG:
                command = new BotLangCommand();
                break;
            case SETBOTLANG:
                command = new SetBotLangCommand();
                break;
            default:
                throw new AssertionError("The passed command type was not found");
        }
        return command;
    }
}
