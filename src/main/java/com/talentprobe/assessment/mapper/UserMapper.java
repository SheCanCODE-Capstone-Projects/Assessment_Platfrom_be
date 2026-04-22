package com.talentprobe.assessment.mapper;

import com.talentprobe.assessment.dto.UserDto;
import com.talentprobe.assessment.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDto dto);

    UserDto toDto(User user);
}