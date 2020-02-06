package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.model.BotUser;
import domain.templates.ButtonTemplate;
import domain.utils.ArgumentsWaiter;
import domain.utils.ButtonSetter;
import domain.utils.LocalizationUtils;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ResourceBundle;
import java.util.StringJoiner;

public class CommandExecutor {
    private static final Logger logger = Logger.getLogger(CommandExecutor.class);
    private ArgumentsWaiter argumentsWaiter = ArgumentsWaiter.getInstance();
    private BotUserService userService = new BotUserService();

    void start(User user, SendMessage response) throws DAOException {
        logger.trace("Executing the start command");
        BotUser botUser = userService.findUser(user.getId());
        if (botUser == null) {
            botUser = new BotUser(user.getId(),
                                  user.getFirstName(),
                                  user.getLastName(),
                                  user.getUserName(),
                                  user.getLanguageCode());
            userService.saveUser(botUser);
        }
        ResourceBundle resBundle = LocalizationUtils.getResourceBundle(botUser.getLanguageCode(), "strings");
        String text = new StringJoiner(" ").add(resBundle.getString("greeting"))
                                           .add(botUser.getFirstName())
                                           .add(botUser.getLastName() + "!")
                                           .add(resBundle.getString("introduction"))
                                           .toString();
        response.setText(text);
        logger.trace("The start command was successfully executed");
    }

    void help(User user, SendMessage response) throws DAOException {
        logger.trace("Executing the help command");
        BotUser botUser = userService.findUser(user.getId());
        CommandType[] commands = CommandType.values();
        StringBuilder text = new StringBuilder();
        ResourceBundle resBundle = LocalizationUtils.getResourceBundle(botUser.getLanguageCode(), "commands");
        text.append(resBundle.getString("header"));
        for (CommandType type : commands) {
            String command = type.getCommand();
            text.append("/").append(command).append(" - ").append(resBundle.getString(command)).append("\n");
        }
        response.setText(text.toString());
        logger.trace("The help command was successfully executed");
    }

    void langInfo(User user, SendMessage response) throws DAOException {
        logger.trace("Executing the langinfo command");
        BotUser botUser = userService.findUser(user.getId());
        String code = botUser.getLanguageCode();
        ResourceBundle resBundle = LocalizationUtils.getResourceBundle(code, "strings");
        String text = new StringJoiner("").add(resBundle.getString("userLang")).add(" ")
                                           .add(LocalizationUtils.getLangName(code, code)).add("\n")
                                           .add(resBundle.getString("targetLang")).add(" ")
                                           .add(LocalizationUtils.getLangName(botUser.getTranslationLang(), code))
                                           .toString();
        response.setText(text);
        logger.trace("The langinfo command was successfully executed");
    }

    void setMyLang(User user, String argument, SendMessage response, Command command) throws DAOException {
        logger.trace("Executing the setmylang command");
        if(argument == null){
            ButtonSetter.setButtons(response, ButtonTemplate.LANGUAGES);
            response.setText(LocalizationUtils.getResourceBundle(user.getLanguageCode(), "strings").getString("chooseLang"));
            argumentsWaiter.waitForArgs(command);
        } else {
            BotUser botUser = userService.findUser(user.getId());
            botUser.setLanguageCode(argument);
            userService.updateUser(botUser);
            String code = botUser.getLanguageCode();
            String text = LocalizationUtils.getStringByKeys(code,"done", "yourLangNow") + LocalizationUtils.getLangName(code, code);
            response.setText(text);
            logger.trace("The setmylang command was successfully executed");
        }
    }

    void toLang(User user, String argument, SendMessage response, Command command) throws DAOException {
        logger.trace("Executing the tolang command");
        if(argument == null){
            ButtonSetter.setButtons(response, ButtonTemplate.LANGUAGES);
            response.setText(LocalizationUtils.getResourceBundle(user.getLanguageCode(), "strings").getString("chooseLang"));
            argumentsWaiter.waitForArgs(command);
        } else {
            BotUser botUser = userService.findUser(user.getId());
            botUser.setTranslationLang(argument);
            userService.updateUser(botUser);
            String code = botUser.getLanguageCode();
            String text = LocalizationUtils.getStringByKeys(code,"done", "yourTargetLangNow")
                    + LocalizationUtils.getLangName(botUser.getTranslationLang(), code);
            response.setText(text);
            logger.trace("The tolang command was successfully executed");
        }
    }
}
