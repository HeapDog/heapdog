package io.heapdog.core;

import io.heapdog.core.dto.SignupRequestDto;
import io.heapdog.core.dto.SignupResponseDto;
import io.heapdog.core.model.HeapDogUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "languages", ignore = true)
    HeapDogUser toEntity(SignupRequestDto dto);
    SignupResponseDto toDto(HeapDogUser user);
}
