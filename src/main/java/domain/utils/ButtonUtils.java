package domain.utils;

import domain.templates.ButtonTemplate;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ButtonUtils {
    private static final Logger logger = Logger.getLogger(ButtonUtils.class);
    private static final int MAX_ROW_SIZE = 4;

    public static void setButtons(SendMessage response, ButtonTemplate template) {
        switch (template) {
            case LANGUAGES:
                setLanguageButtons(response);
                break;
            default:
                throw new AssertionError("Invalid button template");
        }
    }

    private static void setLanguageButtons(SendMessage response) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setOneTimeKeyboard(true);
        response.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        List<String> languages = new ArrayList<>(LocalizationUtils.getAvailableLangs());
        KeyboardRow keyboardRow = null;

        for (int i = 0; i < languages.size(); i++) {
            if (i % MAX_ROW_SIZE == 0) {
                keyboardRow = new KeyboardRow();
                keyboard.add(keyboardRow);
            }
            keyboardRow.add(new KeyboardButton(languages.get(i)));
        }

        replyKeyboardMarkup.setKeyboard(keyboard);
        logger.debug("Language buttons have been successfully set to message " + response.toString());
    }

    public static void removeButtons(SendMessage response){
        response.setReplyMarkup(new ReplyKeyboardRemove());
    }
}
