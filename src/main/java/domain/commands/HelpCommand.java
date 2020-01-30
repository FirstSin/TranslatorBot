package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.model.BotUser;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;
import java.util.ResourceBundle;

public class HelpCommand implements Command {
    private CommandType type = CommandType.HELP;
    private BotUserService userService = new BotUserService();

    public HelpCommand() { }

    public String execute(Message message, String[] args) throws DAOException {
        BotUser botUser = userService.findUser(message.getFrom().getId());
        CommandType[] commands = CommandType.values();
        StringBuilder answer = new StringBuilder();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("internationalization.commands", Locale.forLanguageTag(botUser.getLanguageCode()));
        answer.append(resourceBundle.getString("header"));
        for (CommandType type : commands) {
            String command = type.getCommand();
            answer.append("/" + command + " - " + resourceBundle.getString(command) + "\n");
        }
        return answer.toString();
    }

    public String getCommand() {
        return type.getCommand();
    }

    public String getDescription() {
        return type.getDescription();
    }
}
