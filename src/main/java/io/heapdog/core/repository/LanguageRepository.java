package io.heapdog.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.heapdog.core.model.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findLanguageById(Long id);
    boolean existsLanguageByNameAndVersion(String name, String version);    
}
