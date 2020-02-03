package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.model.BotUser;
import domain.utils.TextHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class CommandExecutor {
    private BotUserService userService = new BotUserService();

    void start(User user, SendMessage response) throws DAOException {
        BotUser botUser = userService.findUser(user.getId());
        if (botUser == null) {
            botUser = new BotUser(user.getId(), user.getFirstName(),
                                  user.getLastName(), user.getUserName(),
                                  user.getLanguageCode());
            userService.saveUser(botUser);
        }

        ResourceBundle resBundle = ResourceBundle.getBundle("internationalization.start",
                                                                 Locale.forLanguageTag(botUser.getLanguageCode()));
        String text = new StringJoiner(" ").add(resBundle.getString("greeting"))
                                               .add(botUser.getFirstName()).add(botUser.getLastName() + "!")
                                               .add(resBundle.getString("firstRun"))
                                               .toString();
        response.setText(text);
    }

    void help(User user, SendMessage response) throws DAOException {
        BotUser botUser = userService.findUser(user.getId());
        CommandType[] commands = CommandType.values();
        StringBuilder text = new StringBuilder();
        ResourceBundle resBundle = ResourceBundle.getBundle("internationalization.commands",
                                                                 Locale.forLanguageTag(botUser.getLanguageCode()));
        text.append(resBundle.getString("header"));
        for (CommandType type : commands) {
            String command = type.getCommand();
            text.append("/" + command + " - " + resBundle.getString(command) + "\n");
        }

        response.setText(text.toString());
    }

    void langInfo(User user, SendMessage response) throws DAOException {
        BotUser botUser = userService.findUser(user.getId());
        ResourceBundle resBundle = ResourceBundle.getBundle("internationalization.langinfo",
                                                            Locale.forLanguageTag(botUser.getLanguageCode()));
        String text = new StringJoiner("").add(resBundle.getString("myLang"))
                                              .add(Locale.forLanguageTag(botUser.getLanguageCode()).getDisplayLanguage(Locale.forLanguageTag(botUser.getLanguageCode()))).add("\n")
                                              .add(resBundle.getString("toLang"))
                                              .add(Locale.forLanguageTag(botUser.getTranslationLang()).getDisplayLanguage(Locale.forLanguageTag(botUser.getLanguageCode())))
                                              .toString();
        response.setText(text);
    }

    void setMyLang(User user, String argument, SendMessage response) throws DAOException {
        BotUser botUser = userService.findUser(user.getId());
        botUser.setLanguageCode(argument);
        userService.updateUser(botUser);
        ResourceBundle resBundle = ResourceBundle.getBundle("internationalization.other", Locale.forLanguageTag(argument));
        String text = new StringJoiner(" ").add(resBundle.getString("done"))
                                               .add(resBundle.getString("myLang"))
                                               .add(Locale.forLanguageTag(botUser.getLanguageCode()).getDisplayLanguage(Locale.forLanguageTag(botUser.getLanguageCode())))
                                               .toString();
        response.setText(text);
    }

    void toLang(User user, String argument, SendMessage response) throws DAOException {
        BotUser botUser = userService.findUser(user.getId());
        botUser.setTranslationLang(argument);
        userService.updateUser(botUser);
        ResourceBundle resBundle = ResourceBundle.getBundle("internationalization.other", Locale.forLanguageTag(botUser.getLanguageCode()));
        String text = new StringJoiner(" ").add(resBundle.getString("done"))
                                               .add(resBundle.getString("translationLang"))
                                               .add(Locale.forLanguageTag(botUser.getTranslationLang()).getDisplayLanguage(Locale.forLanguageTag(botUser.getLanguageCode())))
                                               .toString();
        response.setText(text);
    }
}
