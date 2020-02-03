package domain.commands;

import dao.exceptions.DAOException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

public interface Command {

    void execute(User user, String argument, SendMessage response) throws DAOException;
}
