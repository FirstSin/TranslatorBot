package domain.utils;

import domain.model.Translator;
import domain.model.YandexTranslator;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TextHandler implements Handler {
    private Translator translator = new YandexTranslator();

    @Override
    public BotApiMethod handle(Update update) {
        long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();
        String response = null;
        try{
            response = translator.translate(message, "ru");
        } catch (Exception e){}

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(response);
        sendMessage.setChatId(chatId);
        return sendMessage;
    }
}
