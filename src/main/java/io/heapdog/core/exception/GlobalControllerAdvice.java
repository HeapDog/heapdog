package io.heapdog.core.exception;


import io.heapdog.core.dto.APIError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import io.heapdog.core.exception.UserNotFoundException;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    ResponseEntity<APIError> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<APIError.FieldError> details = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> APIError
                        .FieldError
                        .builder()
                        .field(err.getField())
                        .message(err.getDefaultMessage()).build())
                .toList();

        APIError errorResponse = APIError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("VALIDATION_ERROR")
                .message("Validation failed for request")
                .details(details)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(exception = DuplicateUsernameException.class)
    ResponseEntity<APIError> handleDuplicateUsernameException(DuplicateUsernameException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        APIError errorResponse = APIError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONTINUE.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .code("USERNAME_EXISTS")
                .message(ex.getMessage())
                .details(List.of(APIError.FieldError.builder().field("username").message("Username already exists").build()))
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(exception = BadCredentialsException.class)
    ResponseEntity<APIError> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        APIError errorResponse = APIError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .code("BAD_CREDENTIALS")
                .message("Invalid username or password")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(exception = JwtValidationFailedException.class)
    ResponseEntity<APIError> handleJWTVerificationFailedException(JwtValidationFailedException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        APIError errorResponse = APIError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .code("INVALID_TOKEN")
                .message("Invalid token")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(exception = AuthorizationDeniedException.class)
    ResponseEntity<APIError> handleAuthorizationDeniedException(AuthorizationDeniedException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        APIError errorResponse = APIError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .code("ACCESS_DENIED")
                .message("You do not have permission to access this resource")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
    @ExceptionHandler(exception = InvalidOtpException.class)
    ResponseEntity<APIError> handleInvalidOtpException(InvalidOtpException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        APIError errorResponse = APIError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("Invalid_OTP")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(exception = UserNotFoundException.class)
    public ResponseEntity<APIError> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        APIError errorResponse = APIError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .code("USER_NOT_FOUND")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
