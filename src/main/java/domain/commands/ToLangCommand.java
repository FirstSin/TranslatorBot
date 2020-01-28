package domain.commands;

import dao.exceptions.DAOException;
import dao.services.BotUserService;
import domain.model.BotUser;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ToLangCommand implements Command {
    private BotUserService userService = new BotUserService();
    @Override
    public String execute(Message message, String[] args) throws DAOException {
        BotUser botUser = userService.findUser(message.getFrom().getId());
        botUser.setTranslationLang(args[0]);
        userService.updateUser(botUser);
        return "Done.";
    }
}
