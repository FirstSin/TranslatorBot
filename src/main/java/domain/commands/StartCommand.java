package domain.commands;

import dao.exceptions.DAOException;
import dao.services.UserService;
import domain.model.CommandType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;
import java.util.ResourceBundle;

public class StartCommand implements Command {
    private static final CommandType type = CommandType.START;
    private UserService userService = new UserService();

    public String execute(Message message, String[] args) throws DAOException {
        User currentUser = message.getFrom();
        String langCode = currentUser.getLanguageCode();
        User user = userService.findUser(currentUser.getId());
        String response;
        ResourceBundle resourceBundle;

        if (user == null) {
            userService.saveUser(message.getFrom());
            resourceBundle = ResourceBundle.getBundle("languages.start", Locale.forLanguageTag(langCode));
            response = resourceBundle.getString(
                    "hello") + currentUser.getFirstName() + " " + currentUser.getLastName() + resourceBundle.getString(
                    "firstMeeting") + resourceBundle.getString("introduction");
        } else {
            langCode = user.getLanguageCode();
            resourceBundle = ResourceBundle.getBundle("languages.start", Locale.forLanguageTag(langCode));
            response = resourceBundle.getString(
                    "hello") + currentUser.getFirstName() + " " + currentUser.getLastName() + resourceBundle.getString(
                    "anotherMeeting") + resourceBundle.getString("introduction");;
        }

        return response;
    }

    public String getCommand() {
        return type.getCommand();
    }

    public String getDescription() {
        return type.getDescription();
    }
}
