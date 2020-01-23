package domain.exceptions;

public class CommandNotFoundException extends Exception {

    public CommandNotFoundException() {
        super();
    }

    public CommandNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandNotFoundException(Throwable cause) {
        super(cause);
    }

    public CommandNotFoundException(String message) {
        super(message);
    }
}
