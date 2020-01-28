package domain.commands;

import dao.exceptions.DAOException;
import dao.services.UserService;
import domain.model.CommandType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;
import java.util.ResourceBundle;

public class SetBotLangCommand implements Command {
    private static final CommandType type = CommandType.SETBOTLANG;
    private UserService userService = new UserService();

    @Override
    public String execute(Message message, String[] args) throws DAOException {
        User user = userService.findUser(message.getFrom().getId());
        User updatedUser = new User(user.getId(), user.getFirstName(), user.getBot(), user.getLastName(), user.getUserName(), args[0]);
        userService.updateUser(updatedUser);
        ResourceBundle resBundle = ResourceBundle.getBundle("languages.botlang", Locale.forLanguageTag(args[0]));
        return resBundle.getString("done") + resBundle.getString("currentLang") + args[0];
    }

    public String getCommand() {
        return type.getCommand();
    }

    public String getDescription() {
        return type.getDescription();
    }
}
