package com.electronic.kafedra.bll.mapper;

import com.electronic.kafedra.bll.dto.UserDto;
import com.electronic.kafedra.dal.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "student", ignore = true)

    User toEntity(UserDto dto);
}
