package domain.commands;

public enum CommandType {
    START("start"),
    HELP("help"),
    LANGINFO("langinfo"),
    SETMYLANG("setmylang"),
    TOLANG("tolang"),
    STAT("stat");

    private String commandName;

    CommandType(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
