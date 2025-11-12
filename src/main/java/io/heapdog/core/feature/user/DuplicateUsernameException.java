package io.heapdog.core.feature.user;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String msg) {
        super(msg);
    }
}
