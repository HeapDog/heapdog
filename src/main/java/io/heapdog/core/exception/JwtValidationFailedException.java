package io.heapdog.core.exception;

import org.springframework.security.authentication.BadCredentialsException;

public class JwtValidationFailedException extends BadCredentialsException {
    public JwtValidationFailedException(String msg) {
        super(msg);
    }
}
