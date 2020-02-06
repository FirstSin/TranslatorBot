package domain.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class LocalizationUtils {
    private static Properties languages;

    static {
        languages = new Properties();
        try (InputStream in = new FileInputStream("src/main/resources/languages.properties")) {
            languages.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ResourceBundle getResourceBundle(String code, String name) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("localization." + name, Locale.forLanguageTag(code));
        return resourceBundle;
    }

    public static String getStringByKeys(String langCode, String... args) {
        StringBuilder sb = new StringBuilder();
        ResourceBundle resBundle = getResourceBundle(langCode, "strings");
        for (String str : args) {
            sb.append(resBundle.getString(str) + " ");
        }
        return sb.toString();
    }

    public static String getLangCode(String language) {
        Enumeration<Object> keys = languages.keys();
        while (keys.hasMoreElements()) {
            String key = String.valueOf(keys.nextElement());
            String value = languages.getProperty(key);
            if (value.equalsIgnoreCase(language))
                return key;
        }
        throw new IllegalArgumentException();
    }

    public static String getLangName(String code, String userLang) {
        return Locale.forLanguageTag(code).getDisplayLanguage(Locale.forLanguageTag(userLang));
    }

    public static List<String> getLangCodes() {
        List<String> codes = new ArrayList<>();
        Enumeration<Object> keys = languages.keys();
        while (keys.hasMoreElements()) {
            String key = String.valueOf(keys.nextElement());
            codes.add(key);
        }
        return codes;
    }

    public static List<String> getLanguages() {
        List<String> langs = new ArrayList<>();
        Enumeration<Object> keys = languages.keys();
        while (keys.hasMoreElements()) {
            String key = String.valueOf(keys.nextElement());
            String langName = languages.getProperty(key);
            langs.add(langName);
        }
        return langs;
    }
}