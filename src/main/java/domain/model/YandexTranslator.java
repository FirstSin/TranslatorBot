package domain.model;

import com.google.gson.Gson;
import com.google.inject.internal.asm.$ClassTooLargeException;
import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringJoiner;

public class YandexTranslator implements Translator {
    private static String url;
    private static String key;
    private static final Logger logger = Logger.getLogger(YandexTranslator.class);

    public YandexTranslator() {
        if(url == null || key == null)
            setProperties();
    }

    @Override
    public String translate(String message, String lang) throws IOException {
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
        String text = getTranslatedText(json);
        out.close();
        response.close();
        return text;
    }

    private String getTranslatedText(String json) {
        Gson gson = new Gson();
        Message message = gson.fromJson(json, Message.class);
        logger.debug(new StringJoiner(" ")
                .add("Translation result:\nStatus-code:")
                .add(String.valueOf(message.getCode()))
                .add(", lang:").add(message.getLang())
                .add(", text:").add(message.getText()));
        return message.getText();
    }

    private void setProperties() {
        try (InputStream in = new FileInputStream("src/main/resources/translator.properties")) {
            Properties prop = new Properties();
            prop.load(in);
            url = prop.getProperty("url");
            key = prop.getProperty("key");
        } catch (IOException e) {
            logger.error("An error occurred while loading properties: " + e.getMessage(), e);
        }
    }
}
