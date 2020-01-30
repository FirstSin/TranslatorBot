package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.model.BotUser;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class LangInfoCommand implements Command {
    private static final CommandType type = CommandType.LANGINFO;
    private BotUserService userService = new BotUserService();

    @Override
    public String execute(Message message, String[] args) throws DAOException {
        BotUser botUser = userService.findUser(message.getFrom().getId());
        ResourceBundle resBundle = ResourceBundle.getBundle("internationalization.langinfo", Locale.forLanguageTag(botUser.getLanguageCode()));
        String response = new StringJoiner("").add(resBundle.getString("myLang"))
                                             .add(Locale.forLanguageTag(botUser.getLanguageCode()).getDisplayLanguage()).add("\n")
                                             .add(resBundle.getString("toLang"))
                                             .add(Locale.forLanguageTag(botUser.getTranslationLang()).getDisplayLanguage()).toString();
        return response;
    }

    public String getCommand() {
        return type.getCommand();
    }

    public String getDescription() {
        return type.getDescription();
    }
}
