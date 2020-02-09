package domain.model;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringJoiner;

public class YandexTranslator implements Translator {
    private static String url;
    private static String key;
    private static final Logger logger = Logger.getLogger(YandexTranslator.class);
    private static Translator translator;

    private YandexTranslator() {
        setProperties();
    }

    public static Translator getInstance() {
        if (translator == null) {
            translator = new YandexTranslator();
            logger.info("Translator instance created");
        }
        return translator;
    }

    @Override
    public String translate(String message, String lang) throws IOException {
        logger.debug("Starting translating text: " + message + ". Translation lang: " + lang);
        URL urlObj = new URL(url + "translate?key=" + key);
        HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(new StringJoiner("")
                               .add("text=").add(URLEncoder.encode(message, "UTF-8"))
                               .add("&lang=").add(lang).toString());
        InputStream response = connection.getInputStream();
        String json = new Scanner(response).nextLine();
        String text = getTextFromJson(json);
        out.close();
        response.close();
        logger.debug("Text successfully translated. Translated text: " + text);
        final int textLength = text.split(" ").length;
        for (int i = 0; i < textLength; i++) {
            StatisticsCollector.translatedWordIncrement();
        }
        return text;
    }

    private String getTextFromJson(String json) {
        Gson gson = new Gson();
        Message message = gson.fromJson(json, Message.class);
        return message.getText();
    }

    private static void setProperties() {
        try (InputStream in = new FileInputStream("src/main/resources/translator.properties")) {
            Properties prop = new Properties();
            prop.load(in);
            url = prop.getProperty("url");
            key = prop.getProperty("key");
        } catch (IOException e) {
            logger.error("An error occurred while loading translator properties", e);
        }
        logger.info("Translator properties set successfully");
    }
}
