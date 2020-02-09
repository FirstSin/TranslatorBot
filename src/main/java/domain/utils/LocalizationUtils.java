package domain.utils;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class LocalizationUtils {
    private static final Logger logger = Logger.getLogger(LocalizationUtils.class);
    private static Map<String, String> languages;

    static {
        Properties prop = new Properties();
        try (InputStream in = new FileInputStream("src/main/resources/languages.properties")) {
            prop.load(in);
            languages = parseToMap(prop);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Languages have been successfully loaded from resources");
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
        if (isAvailableCode(language))
            return language;
        for (Map.Entry<String, String> entry : languages.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(language))
                return entry.getKey();
        }

        throw new IllegalArgumentException("Unsupported language");
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
        List<String> langCodes = new ArrayList<>(languages.size());
        languages.forEach((k, v) -> langCodes.add(k));
        return langCodes;
    }

    public static List<String> getLanguages() {
        List<String> langs = new ArrayList<>(languages.size());
        languages.forEach((k, v) -> langs.add(v));
        return langs;
    }

    private static Map<String, String> parseToMap(Properties prop) {
        Map<String, String> map = new HashMap<>(prop.size());
        Enumeration<Object> keys = prop.keys();
        while (keys.hasMoreElements()) {
            String key = String.valueOf(keys.nextElement());
            String value = prop.getProperty(key);
            map.put(key, value);
        }
        return map;
    }
}
