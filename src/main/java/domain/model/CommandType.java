package domain.model;

public enum CommandType {
    START("start", "Start using the bot"),
    HELP("help", "Displays a list of all available commands");

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
