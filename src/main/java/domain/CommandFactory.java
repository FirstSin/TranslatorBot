package domain;

import domain.commands.BotCommand;
import domain.commands.HelpCommand;
import domain.commands.StartCommand;

import javax.validation.constraints.NotNull;

public class CommandFactory {
    public BotCommand getCommand(@NotNull CommandType type){
        BotCommand command = null;
        switch (type){
            case START:
                command = new StartCommand("start","Start using the bot\n");
                break;
            case HELP:
                command = new HelpCommand("help", "Displays a list of all available commands");
                break;
            default:
                throw new AssertionError("The passed command type was not found");
        }
        return command;
    }
}
