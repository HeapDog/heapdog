package io.heapdog.core.shared;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;


@Data
@Builder
public class APIError {
    private Instant timestamp;
    private int status;
    private String error;
    private String code;
    private String message;
    private List<FieldError> details;
    private String path;

    @Data
    @Builder
    public static class FieldError {
        private String field;
        private String message;
    }
}
