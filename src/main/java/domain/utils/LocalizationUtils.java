package domain.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class LocalizationUtils {
    private static final Logger logger = LoggerFactory.getLogger(LocalizationUtils.class);
    private static Map<String, String> languages;
    private static Map<String, ResourceBundle> resBundles;

    static {
        Properties prop = new Properties();
        try (InputStream in = new FileInputStream("src/main/resources/languages.properties")) {
            prop.load(in);
            languages = parseToMap(prop);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Languages have been successfully loaded from resources");
        initResourceBundles();
    }

    private LocalizationUtils() {
        throw new AssertionError("Cannot create an instance of a class");
    }

    public static String langCodeOf(String language) {
        if (isAvailableCode(language))
            return language;
        for (Map.Entry<String, String> entry : languages.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(language))
                return entry.getKey();
        }

        throw new IllegalArgumentException("Unsupported language");
    }

    public static boolean isAvailableCode(String langCode) {
        return languages.containsKey(langCode);
    }

    public static boolean isAvailableLang(String lang) {
        return languages.containsValue(lang);
    }

    public static String getNativeLangName(String code, String userLang) {
        return Locale.forLanguageTag(code).getDisplayLanguage(Locale.forLanguageTag(userLang));
    }

    public static Set<String> getAvailableLangCodes() {
        return languages.keySet();
    }

    public static Collection<String> getAvailableLangs() {
        return languages.values();
    }

    public static ResourceBundle getResourceBundleByCode(String code){
        return resBundles.get(code);
    }

    private static Map<String, String> parseToMap(Properties prop) {
        Map<String, String> map = new HashMap<>(prop.size());
        prop.forEach((k, v) -> map.put(String.valueOf(k), String.valueOf(v)));
        return map;
    }

    private static void initResourceBundles(){
        resBundles = new HashMap<>(16);
        getAvailableLangCodes().forEach(code -> {
            resBundles.put(code, ResourceBundle.getBundle("localization.strings", Locale.forLanguageTag(code)));
        });
    }
}
