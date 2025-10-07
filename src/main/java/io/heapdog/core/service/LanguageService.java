package io.heapdog.core.service;

import io.heapdog.core.dto.LanguageRequestDTO;
import io.heapdog.core.dto.LanguageResponseDTO;
import io.heapdog.core.model.HeapDogUser;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import io.heapdog.core.LanguageMapper;
import io.heapdog.core.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository repository;
    private final LanguageMapper mapper;

    public LanguageResponseDTO createLanguage(LanguageRequestDTO dto, HeapDogUser user) {
        var entity = mapper.toEntity(dto);
        entity.setCreatedBy(user);
        var saved = repository.save(entity);

        return mapper.toDto(saved);
    }

    public boolean existsByNameAndVersion(String name, String version) {
        return repository.existsLanguageByNameAndVersion(name, version);
    }

    public LanguageResponseDTO getLanguageById(Long id) {
        var entity = repository.findLanguageById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found with id: " + id));
        return mapper.toDto(entity);
    }
}
