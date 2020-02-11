package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.model.BotUser;
import domain.utils.StatisticsCollector;
import domain.templates.ButtonTemplate;
import domain.templates.ErrorMessage;
import domain.utils.ArgumentsWaiter;
import domain.utils.ButtonSetter;
import domain.utils.ErrorMessageUtils;
import domain.utils.LocalizationUtils;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class CommandExecutor {
    private static final Logger logger = Logger.getLogger(CommandExecutor.class);
    private ArgumentsWaiter argumentsWaiter = ArgumentsWaiter.getInstance();
    private BotUserService userService = new BotUserService();

    public void start(User user, SendMessage response) throws DAOException {
        BotUser botUser = userService.findUser(user.getId());
        if (botUser == null) {
            botUser = new BotUser(user.getId(),
                                  user.getFirstName(),
                                  user.getLastName(),
                                  user.getUserName(),
                                  user.getLanguageCode());
            userService.saveUser(botUser);
            StatisticsCollector.userIncrement();
        }
        ResourceBundle resBundle = LocalizationUtils.getResourceBundle(botUser.getLanguageCode(), "strings");
        String text = new StringJoiner(" ").add(resBundle.getString("greeting"))
                                           .add(botUser.getFirstName())
                                           .add(botUser.getLastName() + "!")
                                           .add(resBundle.getString("introduction"))
                                           .toString();
        response.setText(text);
    }

    public void help(User user, SendMessage response) throws DAOException {
        BotUser botUser = userService.findUser(user.getId());
        StringBuilder text = new StringBuilder();
        ResourceBundle resBundle = LocalizationUtils.getResourceBundle(botUser.getLanguageCode(), "commands");

        text.append("<b>").append(resBundle.getString("header")).append(":</b>\n");
        for (CommandType type : CommandType.values()) {
            String command = type.getCommandName();
            if(command.equalsIgnoreCase("stat"))
                continue;
            text.append("/").append(command).append(" - ").append(resBundle.getString(command)).append("\n");
        }
        response.setText(text.toString());
    }

    public void langInfo(User user, SendMessage response) throws DAOException {
        BotUser botUser = userService.findUser(user.getId());
        String code = botUser.getLanguageCode();
        ResourceBundle resBundle = LocalizationUtils.getResourceBundle(code, "strings");
        String text = resBundle.getString("userLang") + " " +
                LocalizationUtils.getLangName(code, code) + "\n" +
                resBundle.getString("targetLang") + " " +
                LocalizationUtils.getLangName(botUser.getTranslationLang(), code);
        response.setText(text);
    }

    public void setMyLang(User user, String argument, SendMessage response, Command command) throws DAOException {
        BotUser botUser = userService.findUser(user.getId());
        if (argument == null) {
            ButtonSetter.setButtons(response, ButtonTemplate.LANGUAGES);
            response.setText(LocalizationUtils.getResourceBundle(botUser.getLanguageCode(), "strings").getString("chooseLang"));
            argumentsWaiter.waitForArgs(user.getId(), command);
            logger.trace("The setmylang command is waiting for arguments...");
            return;
        }

        String lang;
        try {
            lang = LocalizationUtils.getLangCode(argument);
        } catch (IllegalArgumentException e) {
            logger.error("An error occurred when determining the language code", e);
            response.setText(ErrorMessageUtils.getMessage(botUser.getLanguageCode(), ErrorMessage.UNKNOWN_LANG));
            return;
        }

        botUser.setLanguageCode(lang);
        userService.updateUser(botUser);
        String code = botUser.getLanguageCode();
        String text = LocalizationUtils.getStringByKeys(code, "done", "yourLangNow") + LocalizationUtils.getLangName(code, code);
        response.setText(text);
        response.setReplyMarkup(new ReplyKeyboardRemove());
    }

    public void toLang(User user, String argument, SendMessage response, Command command) throws DAOException {
        BotUser botUser = userService.findUser(user.getId());
        if (argument == null) {
            ButtonSetter.setButtons(response, ButtonTemplate.LANGUAGES);
            response.setText(LocalizationUtils.getResourceBundle(botUser.getLanguageCode(), "strings").getString("chooseLang"));
            argumentsWaiter.waitForArgs(user.getId(), command);
            logger.trace("The tolang command is waiting for arguments...");
            return;
        }

        String lang;
        try {
            lang = LocalizationUtils.getLangCode(argument);
        } catch (IllegalArgumentException e){
            logger.error("An error occurred when determining the language code", e);
            response.setText(ErrorMessageUtils.getMessage(botUser.getLanguageCode(), ErrorMessage.UNKNOWN_LANG));
            return;
        }

        botUser.setTranslationLang(lang);
        userService.updateUser(botUser);
        String code = botUser.getLanguageCode();
        String text = LocalizationUtils.getStringByKeys(code, "done", "yourTargetLangNow")
                + LocalizationUtils.getLangName(botUser.getTranslationLang(), code);
        response.setText(text);
        response.setReplyMarkup(new ReplyKeyboardRemove());
    }

    public void stat(SendMessage response){
        Properties prop;
        try (InputStream in = new FileInputStream("src/main/resources/statistic.properties")) {
            prop = new Properties();
            prop.load(in);
        } catch (IOException e) {
            logger.error("An error occurred while loading statistic properties", e);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<b>Статистика вызова команд:</b>\n");
        for (CommandType type: CommandType.values()) {
            sb.append(type.getCommandName()).append(": ").append(prop.get(type.getCommandName())).append("\n");
        }
        sb.append("\n<b>Общая статистика:</b>\n");
        sb.append("Пользователей бота: ").append(prop.get("users"));
        sb.append("\nПереведено слов: ").append(prop.get("translatedWords"));
        response.setText(sb.toString());
    }
}
