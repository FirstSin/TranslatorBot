package domain.model;

import java.util.Arrays;

public class Message {
    private int code;
    private String lang;
    private String[] text;

    public int getCode() {
        return code;
    }

    public String getLang() {
        return lang;
    }

    public String getText() {
        StringBuilder sb = new StringBuilder(text.length);
        for (String str : text) {
            sb.append(str);
        }
        return sb.toString();
    }
}
