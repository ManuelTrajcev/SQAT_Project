package mk.ukim.finki.wp.workspaces.model.exceptions;

import jakarta.servlet.http.HttpServletResponse;

public class UnauthorizedException extends RuntimeException {
    private final int status;

    public UnauthorizedException(String message) {
        super(message);
        this.status = HttpServletResponse.SC_UNAUTHORIZED;
    }

    public int getStatus() {
        return status;
    }
}
