package domain.utils;

import domain.templates.ErrorMessage;

import java.util.ResourceBundle;

public class ErrorMessageUtils {
    public static String getMessage(String langCode, ErrorMessage errorMsg) {
        switch (errorMsg) {
            case UNKNOWN_LANG:
                return unknownLanguage(langCode);
            default:
                throw new AssertionError("Invalid error message template");
        }
    }

    private static String unknownLanguage(String langCode) {
        ResourceBundle resourceBundle = LocalizationUtils.getResourceBundle(langCode, "strings");
        return resourceBundle.getString("unknownLanguage");
    }
}
