package domain.commands;

public enum CommandType {
    START("start", "Start using the bot"),
    HELP("help", "Displays a list of all available commands"),
    LANGINFO("langinfo", "Displays the language of the bot"),
    SETMYLANG("setmylang", "Changes the language of the bot"),
    TOLANG("tolang", "Set translation language"),
    STAT("stat", "description");

    private String command;
    private String description;

    CommandType(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}
