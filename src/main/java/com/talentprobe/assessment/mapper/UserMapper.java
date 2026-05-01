package com.talentprobe.assessment.mapper;

import com.talentprobe.assessment.dto.UserDto;
import com.talentprobe.assessment.dto.UserResponseDto;
import com.talentprobe.assessment.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "idDocument", ignore = true)
    @Mapping(target = "idDocumentName", ignore = true)
    @Mapping(target = "idDocumentType", ignore = true)
    @Mapping(target = "activatedAt", ignore = true)
    @Mapping(target = "deactivatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    User toEntity(UserDto dto);

    UserResponseDto toResponseDto(User user);
}