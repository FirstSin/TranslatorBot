package domain.utils;

import domain.templates.ErrorMessageTemplate;

import java.util.ResourceBundle;

public class ErrorMessageUtils {
    public static String getMessage(String langCode, ErrorMessageTemplate errorMsg) {
        switch (errorMsg) {
            case UNKNOWN_LANG:
                return unknownLanguage(langCode);
            default:
                throw new AssertionError("Invalid error message template");
        }
    }

    private static String unknownLanguage(String langCode) {
        ResourceBundle resourceBundle = LocalizationUtils.getResourceBundleByCode(langCode);
        return resourceBundle.getString("unknownLanguage");
    }
}
