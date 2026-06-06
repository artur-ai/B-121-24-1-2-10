package com.electronic.kafedra.bll.mapper;

import com.electronic.kafedra.bll.dto.UserDto;
import com.electronic.kafedra.dal.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User entity);
    User toEntity(UserDto dto);
}
