package domain.utils;

import domain.CommandFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class CommandHandlerImpl implements CommandHandler{

    private CommandFactory factory;

    @Override
    public void handle(Update update) {
        int userId = update.getMessage().getFrom().getId();
        long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();
    }
}
