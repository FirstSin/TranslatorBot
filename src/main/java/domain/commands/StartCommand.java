package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.model.CommandType;
import org.telegram.telegrambots.meta.api.objects.Message;
import domain.model.BotUser;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;
import java.util.ResourceBundle;

public class StartCommand implements Command {
    private static final CommandType type = CommandType.START;
    private BotUserService userService = new BotUserService();

    public String execute(Message message, String[] args) throws DAOException {
        User currentUser = message.getFrom();
        BotUser botUser = userService.findUser(currentUser.getId());
        String response;
        ResourceBundle resourceBundle;

        if (botUser == null) {
            botUser = new BotUser(currentUser.getId(), currentUser.getFirstName(), currentUser.getLastName(), currentUser.getUserName(), currentUser.getLanguageCode());
            userService.saveUser(botUser);
            resourceBundle = ResourceBundle.getBundle("languages.start", Locale.forLanguageTag(botUser.getLanguageCode()));
            response = resourceBundle.getString(
                    "hello") + currentUser.getFirstName() + " " + currentUser.getLastName() + resourceBundle.getString(
                    "firstMeeting") + resourceBundle.getString("introduction");
        } else {
            resourceBundle = ResourceBundle.getBundle("languages.start", Locale.forLanguageTag(botUser.getLanguageCode()));
            response = resourceBundle.getString(
                    "hello") + currentUser.getFirstName() + " " + currentUser.getLastName() + resourceBundle.getString(
                    "anotherMeeting") + resourceBundle.getString("introduction");
            ;
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
