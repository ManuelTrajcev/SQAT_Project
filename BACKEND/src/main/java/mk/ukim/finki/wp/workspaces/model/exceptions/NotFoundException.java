package mk.ukim.finki.wp.workspaces.model.exceptions;

import jakarta.servlet.http.HttpServletResponse;

public class NotFoundException extends RuntimeException {
    private final int status;

    public NotFoundException(String message) {
        super(message);
        this.status = HttpServletResponse.SC_NOT_FOUND;
    }

    public int getStatus() {
        return status;
    }
}
