package io.heapdog.core.feature.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    HeapDogUser toEntity(SignupRequestDto dto);
    SignupResponseDto toDto(HeapDogUser user);
}
