package domain.model;

public class Statistics {
    private long startCount;
    private long helpCount;
    private long langInfoCount;
    private long setMyLangCount;
    private long toLangCount;
    private long usersCount;
    private long translatedWordsCount;

    public Statistics(long startCount, long helpCount, long langInfoCount, long setMyLangCount, long toLangCount, long usersCount, long translatedWordsCount) {
        this.startCount = startCount;
        this.helpCount = helpCount;
        this.langInfoCount = langInfoCount;
        this.setMyLangCount = setMyLangCount;
        this.toLangCount = toLangCount;
        this.usersCount = usersCount;
        this.translatedWordsCount = translatedWordsCount;
    }

    public Statistics() {
    }

    public long getStartCount() {
        return startCount;
    }

    public void setStartCount(long startCount) {
        this.startCount = startCount;
    }

    public long getHelpCount() {
        return helpCount;
    }

    public void setHelpCount(long helpCount) {
        this.helpCount = helpCount;
    }

    public long getLangInfoCount() {
        return langInfoCount;
    }

    public void setLangInfoCount(long langInfoCount) {
        this.langInfoCount = langInfoCount;
    }

    public long getToLangCount() {
        return toLangCount;
    }

    public void setToLangCount(long toLangCount) {
        this.toLangCount = toLangCount;
    }

    public long getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(long usersCount) {
        this.usersCount = usersCount;
    }

    public long getTranslatedWordsCount() {
        return translatedWordsCount;
    }

    public void setTranslatedWordsCount(long translatedWordsCount) {
        this.translatedWordsCount = translatedWordsCount;
    }

    public long getSetMyLangCount() {
        return setMyLangCount;
    }

    public void setSetMyLangCount(long setMyLangCount) {
        this.setMyLangCount = setMyLangCount;
    }
}
