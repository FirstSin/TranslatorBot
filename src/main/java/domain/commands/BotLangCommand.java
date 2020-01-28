package domain.commands;

import dao.exceptions.DAOException;
import dao.services.UserService;
import domain.model.CommandType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;
import java.util.ResourceBundle;

public class BotLangCommand implements Command {
    private static final CommandType type = CommandType.BOTLANG;
    private UserService userService = new UserService();

    @Override
    public String execute(Message message, String[] args) throws DAOException {
        User user = userService.findUser(message.getFrom().getId());
        ResourceBundle resBundle = ResourceBundle.getBundle("languages.botlang", Locale.forLanguageTag(user.getLanguageCode()));
        return resBundle.getString("currentLang") + user.getLanguageCode();
    }

    public String getCommand() {
        return type.getCommand();
    }

    public String getDescription() {
        return type.getDescription();
    }
}
