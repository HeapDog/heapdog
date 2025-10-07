package io.heapdog.core.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LanguageRequestDTO {
    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Version must not be blank")
    private String version;
}
