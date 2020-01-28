package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.model.BotUser;
import domain.model.CommandType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;
import java.util.ResourceBundle;

public class SetBotLangCommand implements Command {
    private static final CommandType type = CommandType.SETBOTLANG;
    private BotUserService userService = new BotUserService();

    @Override
    public String execute(Message message, String[] args) throws DAOException {
        BotUser botUser = userService.findUser(message.getFrom().getId());
        botUser.setLanguageCode(args[0]);
        userService.updateUser(botUser);
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
