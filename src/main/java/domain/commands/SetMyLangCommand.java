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

public class SetMyLangCommand implements Command {
    private static final CommandType type = CommandType.SETMYLANG;
    private BotUserService botUserService = new BotUserService();
    private ArgumentRequester argumentRequester = ArgumentRequester.getInstance();

    @Override
    public void execute(User user, String argument, SendMessage response) throws DAOException {
        BotUser botUser = botUserService.findUser(user.getId());

        if (argument != null) {
            try {
                String code = LocalizationUtils.langCodeOf(argument);
                ResourceBundle resBundle = LocalizationUtils.getResourceBundleByCode(code);
                StringJoiner resp = new StringJoiner(" ");

                botUser.setLanguageCode(code);
                botUserService.updateUser(botUser);
                resp.add(resBundle.getString("done"))
                    .add(resBundle.getString("yourLangNow"))
                    .add(LocalizationUtils.getNativeLangName(code, code));
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
