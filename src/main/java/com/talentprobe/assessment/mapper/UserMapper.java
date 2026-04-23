package com.talentprobe.assessment.mapper;

import com.talentprobe.assessment.dto.AdminDto;
import com.talentprobe.assessment.dto.UserDto;
import com.talentprobe.assessment.dto.UserResponseDto;
import com.talentprobe.assessment.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // UserDto → User for Candidate creation
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "idDocumentPath", ignore = true) //  Null until uploaded
    User toEntity(UserDto dto);
    //admin
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "role", ignore = true) //Service sets Role.ADMIN
    @Mapping(target = "status", ignore = true) // Service sets Status.ACTIVE
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "idDocumentPath", ignore = true) //  Admin has no ID
    User toEntity(AdminDto dto);

    UserResponseDto toDto(User user);
}