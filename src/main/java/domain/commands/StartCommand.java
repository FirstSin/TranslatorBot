package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.model.BotUser;
import domain.utils.LocalizationUtils;
import domain.utils.StatisticsCollector;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ResourceBundle;
import java.util.StringJoiner;

public class StartCommand implements Command {
    private static final CommandType type = CommandType.START;
    private BotUserService botUserService = new BotUserService();

    @Override
    public void execute(User user, String argument, SendMessage response) throws DAOException {
        BotUser botUser = botUserService.findUser(user.getId());

        if (botUser == null) {
            botUser = new BotUser(user.getId(), user.getFirstName(), user.getLastName(), user.getUserName(), user.getLanguageCode());
            botUserService.saveUser(botUser);
            StatisticsCollector.incrementUserCount();
        }

        ResourceBundle resBundle = LocalizationUtils.getResourceBundleByCode(botUser.getLanguageCode());
        String resp = new StringJoiner(" ").add(resBundle.getString("greeting"))
                                           .add(botUser.getFirstName())
                                           .add((botUser.getLastName() != null ? botUser.getLastName() : "\b") + "!")
                                           .add(resBundle.getString("introduction"))
                                           .toString();
        response.setText(resp);
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
