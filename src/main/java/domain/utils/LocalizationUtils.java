package domain.utils;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class LocalizationUtils {
    private static final Logger logger = Logger.getLogger(LocalizationUtils.class);
    private static Properties languages;

    static {
        languages = new Properties();
        try (InputStream in = new FileInputStream("src/main/resources/languages.properties")) {
            languages.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.trace("Languages have been successfully loaded from resources");
    }

    private LocalizationUtils() {
        throw new AssertionError("Cannot create an instance of a class");
    }

    public static ResourceBundle getResourceBundle(String code, String name) {
        return ResourceBundle.getBundle("localization." + name, Locale.forLanguageTag(code));
    }

    public static String getStringByKeys(String langCode, String... args) {
        StringBuilder sb = new StringBuilder();
        ResourceBundle resBundle = getResourceBundle(langCode, "strings");
        for (String str : args) {
            sb.append(resBundle.getString(str)).append(" ");
        }
        return sb.toString();
    }

    public static String getLangCode(String language) {
        Enumeration<Object> keys = languages.keys();
        while (keys.hasMoreElements()) {
            String key = String.valueOf(keys.nextElement());
            String value = languages.getProperty(key);
            if (value.equalsIgnoreCase(language)) {
                return key;
            }
        }
        return null;
    }

    public static boolean isAvailableCode(String langCode) {
        return getLangCodes().contains(langCode);
    }

    public static boolean isAvailableLang(String lang) {
        return getLanguages().contains(lang);
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
