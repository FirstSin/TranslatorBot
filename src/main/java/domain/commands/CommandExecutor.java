package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import dao.services.StatisticsService;
import domain.model.BotUser;
import domain.model.Statistics;
import domain.templates.ButtonTemplate;
import domain.templates.ErrorMessage;
import domain.utils.*;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.ResourceBundle;
import java.util.StringJoiner;

public class CommandExecutor {
    private static final Logger logger = Logger.getLogger(CommandExecutor.class);
    private ArgumentsWaiter argumentsWaiter = ArgumentsWaiter.getInstance();
    private BotUserService userService = new BotUserService();
    private StatisticsService statService = new StatisticsService();

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
        try {
            Statistics statistics = statService.getStatistics();
            StringBuilder sb = new StringBuilder();
            sb.append("<b>Статистика вызова команд:</b>\n");
            sb.append("start: ").append(statistics.getStartCount());
            sb.append("\nhelp: ").append(statistics.getHelpCount());
            sb.append("\nlanginfo: ").append(statistics.getLangInfoCount());
            sb.append("\nsetmylang: ").append(statistics.getSetMyLangCount());
            sb.append("\ntolang: ").append(statistics.getToLangCount());
            sb.append("\n\n<b>Общая статистика:</b>\n");
            sb.append("Пользователей бота: ").append(statistics.getUsersCount());
            sb.append("\nПереведено слов: ").append(statistics.getTranslatedWordsCount());
            response.setText(sb.toString());
        } catch (DAOException e) {
            logger.error("Can't get statistics", e);
        }
    }
}
