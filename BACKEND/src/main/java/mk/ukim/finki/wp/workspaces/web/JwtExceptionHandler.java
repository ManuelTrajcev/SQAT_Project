package mk.ukim.finki.wp.workspaces.web;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.wp.workspaces.dto.JwtExceptionResponse;
import mk.ukim.finki.wp.workspaces.model.exceptions.AccessDeniedException;
import mk.ukim.finki.wp.workspaces.model.exceptions.NotFoundException;
import mk.ukim.finki.wp.workspaces.model.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Hidden
public class JwtExceptionHandler {

    private ResponseEntity<JwtExceptionResponse> buildJwtExceptionResponse(
            HttpStatus status,
            String message,
            String path
    ) {
        return new ResponseEntity<>(
                new JwtExceptionResponse(
                        status.value(),
                        status.getReasonPhrase(),
                        message,
                        path
                ),
                status
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<JwtExceptionResponse> handleUnauthorizedException(
            UnauthorizedException exception,
            HttpServletRequest request
    ) {
        return buildJwtExceptionResponse(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<JwtExceptionResponse> handleAccessDeniedException(
            AccessDeniedException exception,
            HttpServletRequest request
    ) {
        return buildJwtExceptionResponse(
                HttpStatus.FORBIDDEN,
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<JwtExceptionResponse> handleNotFoundException(
            NotFoundException exception,
            HttpServletRequest request
    ) {
        return buildJwtExceptionResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI()
        );
    }


}
