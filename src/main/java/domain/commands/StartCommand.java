package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import org.telegram.telegrambots.meta.api.objects.Message;
import domain.model.BotUser;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class StartCommand implements Command {
    private static final CommandType type = CommandType.START;
    private BotUserService userService = new BotUserService();

    public String execute(Message message, String[] args) throws DAOException {
        User currentUser = message.getFrom();
        BotUser botUser = userService.findUser(currentUser.getId());
        String response;
        if (botUser == null) {
            botUser = new BotUser(currentUser.getId(), currentUser.getFirstName(),
                                  currentUser.getLastName(), currentUser.getUserName(),
                                  currentUser.getLanguageCode());
            userService.saveUser(botUser);
            response = makeResponse(botUser, true);
        }
        else
            response = makeResponse(botUser, false);

        return response;
    }

    public String getCommand() {
        return type.getCommand();
    }

    public String getDescription() {
        return type.getDescription();
    }

    private String makeResponse(BotUser botUser, boolean firstRun) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("internationalization.start", Locale.forLanguageTag(botUser.getLanguageCode()));
        String response = new StringJoiner(" ").add(resourceBundle.getString("greeting"))
                                               .add(botUser.getFirstName()).add(botUser.getLastName() + "!")
                                               .add(resourceBundle.getString(firstRun ? "firstRun" : "alreadyLaunched"))
                                               .toString();
        return response;
    }
}
