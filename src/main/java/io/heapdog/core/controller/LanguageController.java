package io.heapdog.core.controller;

import io.heapdog.core.dto.LanguageRequestDTO;
import io.heapdog.core.model.HeapDogUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.heapdog.core.dto.LanguageResponseDTO;
import io.heapdog.core.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/languages")
@RequiredArgsConstructor
public class LanguageController {
    
    private final LanguageService service;

    @PostMapping
    ResponseEntity<LanguageResponseDTO> createLanguage(@Valid @RequestBody LanguageRequestDTO dto,
                                                       Authentication authentication) {

        if (service.existsByNameAndVersion(dto.getName(), dto.getVersion())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        var created = service.createLanguage(dto, (HeapDogUser) authentication.getPrincipal());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageResponseDTO> getMethodName(@PathVariable String id) {
        var language = service.getLanguageById(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body(language);
    }
}
