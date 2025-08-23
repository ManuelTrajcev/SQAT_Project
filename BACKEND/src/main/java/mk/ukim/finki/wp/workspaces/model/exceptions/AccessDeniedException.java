package mk.ukim.finki.wp.workspaces.model.exceptions;

public class AccessDeniedException extends RuntimeException {
    private final String message;
    private final int statusCode;

    public AccessDeniedException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
