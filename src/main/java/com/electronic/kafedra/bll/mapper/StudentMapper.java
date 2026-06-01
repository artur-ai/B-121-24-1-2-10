package com.electronic.kafedra.bll.mapper;

import com.electronic.kafedra.bll.dto.StudentDto;
import com.electronic.kafedra.dal.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "group.name", target = "groupName")
    @Mapping(source = "user.id", target = "userId")
    StudentDto toDto(Student entity);

    @Mapping(target = "group", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "grades", ignore = true)
    Student toEntity(StudentDto dto);

    List<StudentDto> toDtoList(List<Student> entities);
}
