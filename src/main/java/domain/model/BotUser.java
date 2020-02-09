package domain.model;

public class BotUser {
    private int userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String languageCode;
    private String translationLang = "en";

    public BotUser(int userId, String firstName, String lastName, String userName, String languageCode) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.languageCode = languageCode;
    }

    public BotUser(int userId, String firstName, String lastName, String userName, String languageCode, String translationLang) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.languageCode = languageCode;
        this.translationLang = translationLang;
    }

    public int getId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getTranslationLang() {
        return translationLang;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public void setTranslationLang(String translationLang) {
        this.translationLang = translationLang;
    }
}
