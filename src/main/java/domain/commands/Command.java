package domain.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Command {

    SendMessage execute(String[] args);
}
