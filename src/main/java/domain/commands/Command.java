package domain.commands;

import dao.exceptions.DAOException;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Command {

    String execute(Message message, String[] args) throws DAOException;
}
