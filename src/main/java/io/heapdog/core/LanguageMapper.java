package io.heapdog.core;

import io.heapdog.core.dto.LanguageRequestDTO;
import io.heapdog.core.dto.LanguageResponseDTO;
import io.heapdog.core.model.HeapDogUser;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.heapdog.core.model.Language;

@Mapper(componentModel = "spring")
public interface LanguageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Language toEntity(LanguageRequestDTO dto);
    @Mapping(target = "createdBy", expression = "java(toOwnerDto(language.getCreatedBy()))")
    LanguageResponseDTO toDto(Language language);


    default LanguageResponseDTO.Owner toOwnerDto(@NonNull HeapDogUser user) {
        return LanguageResponseDTO.Owner.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

}