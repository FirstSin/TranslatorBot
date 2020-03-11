package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.model.BotUser;
import domain.utils.LocalizationUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ResourceBundle;

public class HelpCommand implements Command {
    private CommandType type = CommandType.HELP;
    private BotUserService botUserService = new BotUserService();

    @Override
    public void execute(User user, String argument, SendMessage response) throws DAOException {
        BotUser botUser = botUserService.findUser(user.getId());
        StringBuilder resp = new StringBuilder();
        ResourceBundle resBundle = LocalizationUtils.getResourceBundleByCode(botUser.getLanguageCode());

        resp.append("<b>").append(resBundle.getString("header")).append(":</b>\n");
        for (CommandType type : CommandType.values()) {
            String command = type.toString().toLowerCase();
            if (command.equalsIgnoreCase("stat")) continue;
            resp.append(String.format("/%s - %s%n", command, resBundle.getString("command." + command)));
        }
        response.setText(resp.toString());
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
