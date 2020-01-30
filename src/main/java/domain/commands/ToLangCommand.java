package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.model.BotUser;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class ToLangCommand implements Command {
    private BotUserService userService = new BotUserService();
    @Override
    public String execute(Message message, String[] args) throws DAOException {
        BotUser botUser = userService.findUser(message.getFrom().getId());
        botUser.setTranslationLang(args[0]);
        userService.updateUser(botUser);
        ResourceBundle resBundle = ResourceBundle.getBundle("internationalization.other", Locale.forLanguageTag(botUser.getLanguageCode()));
        String response = new StringJoiner(" ").add(resBundle.getString("done"))
                                               .add(resBundle.getString("translationLang"))
                                               .add(Locale.forLanguageTag(botUser.getTranslationLang()).getDisplayLanguage(Locale.forLanguageTag(botUser.getLanguageCode())))
                                               .toString();
        return response;
    }
}
