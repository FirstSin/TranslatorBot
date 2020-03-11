package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.model.BotUser;
import domain.templates.ButtonTemplate;
import domain.templates.ErrorMessageTemplate;
import domain.utils.ArgumentRequester;
import domain.utils.ButtonUtils;
import domain.utils.ErrorMessageUtils;
import domain.utils.LocalizationUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ResourceBundle;
import java.util.StringJoiner;

public class ToLangCommand implements Command {
    private static CommandType type = CommandType.TOLANG;
    private BotUserService botUserService = new BotUserService();
    private ArgumentRequester argumentRequester = ArgumentRequester.getInstance();

    @Override
    public void execute(User user, String argument, SendMessage response) throws DAOException {
        BotUser botUser = botUserService.findUser(user.getId());

        if (argument != null) {
            try {
                String targetLang = LocalizationUtils.langCodeOf(argument);
                String code = botUser.getLanguageCode();
                ResourceBundle resBundle = LocalizationUtils.getResourceBundleByCode(code);
                StringJoiner resp = new StringJoiner(" ");

                botUser.setTranslationLang(targetLang);
                botUserService.updateUser(botUser);
                botUserService.updateUser(botUser);
                resp.add(resBundle.getString("done"))
                    .add(resBundle.getString("yourTargetLangNow"))
                    .add(LocalizationUtils.getNativeLangName(botUser.getTranslationLang(), code));
                response.setText(resp.toString());
                argumentRequester.deleteArgumentRequest(botUser.getId());
                ButtonUtils.removeButtons(response);
            } catch (IllegalArgumentException e) {
                response.setText(ErrorMessageUtils.getMessage(botUser.getLanguageCode(), ErrorMessageTemplate.UNKNOWN_LANG));
            }
        } else {
            setLanguageList(botUser, response);
        }
    }

    @Override
    public String toString() {
        return type.toString();
    }

    private void setLanguageList(BotUser botUser, SendMessage response) {
        ResourceBundle resBundle = LocalizationUtils.getResourceBundleByCode(botUser.getLanguageCode());

        ButtonUtils.setButtons(response, ButtonTemplate.LANGUAGES);
        response.setText(resBundle.getString("chooseLang"));
        argumentRequester.requestArgument(botUser.getId(), this);
    }
}
