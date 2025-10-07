package io.heapdog.core.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LanguageResponseDTO {
    private Long id;
    private String name;
    private String version;
    private Owner createdBy;

    @Builder
    @Data
    public static class Owner {
        private Long id;
        private String username;
    }

}
