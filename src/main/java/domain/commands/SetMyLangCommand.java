package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.model.BotUser;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class SetMyLangCommand implements Command {
    private static final CommandType type = CommandType.SETMYLANG;
    private BotUserService userService = new BotUserService();

    @Override
    public String execute(Message message, String[] args) throws DAOException {
        BotUser botUser = userService.findUser(message.getFrom().getId());
        botUser.setLanguageCode(args[0]);
        userService.updateUser(botUser);
        ResourceBundle resBundle = ResourceBundle.getBundle("internationalization.other", Locale.forLanguageTag(args[0]));
        String response = new StringJoiner(" ").add(resBundle.getString("done"))
                                               .add(resBundle.getString("myLang"))
                                               .add(Locale.forLanguageTag(botUser.getLanguageCode()).getDisplayLanguage(Locale.forLanguageTag(botUser.getLanguageCode())))
                                               .toString();
        return response;
    }

    public String getCommand() {
        return type.getCommand();
    }

    public String getDescription() {
        return type.getDescription();
    }
}
