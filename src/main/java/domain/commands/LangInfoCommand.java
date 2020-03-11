package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.model.BotUser;
import domain.utils.LocalizationUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ResourceBundle;

public class LangInfoCommand implements Command {
    private static final CommandType type = CommandType.LANGINFO;
    private BotUserService botUserService = new BotUserService();

    @Override
    public void execute(User user, String argument, SendMessage response) throws DAOException {
        BotUser botUser = botUserService.findUser(user.getId());
        String code = botUser.getLanguageCode();
        ResourceBundle resBundle = LocalizationUtils.getResourceBundleByCode(code);
        StringBuilder resp = new StringBuilder();
        resp.append(resBundle.getString("userLang")).append(" ")
            .append(LocalizationUtils.getNativeLangName(code, code)).append("\n")
            .append(resBundle.getString("targetLang")).append(" ")
            .append(LocalizationUtils.getNativeLangName(botUser.getTranslationLang(), code));
        response.setText(resp.toString());
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
