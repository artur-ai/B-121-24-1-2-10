package com.electronic.kafedra.bll.mapper;

import com.electronic.kafedra.bll.dto.GradeDto;
import com.electronic.kafedra.dal.entity.Grade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GradeMapper {

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.firstName", target = "studentFirstName")
    @Mapping(source = "student.lastName", target = "studentLastName")
    @Mapping(source = "discipline.id", target = "disciplineId")
    @Mapping(source = "discipline.name", target = "disciplineName")
    @Mapping(source = "teacher.id", target = "teacherId")
    GradeDto toDto(Grade entity);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "discipline", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    Grade toEntity(GradeDto dto);

    List<GradeDto> toDtoList(List<Grade> entities);
}
