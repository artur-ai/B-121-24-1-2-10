package com.electronic.kafedra.bll.mapper;

import com.electronic.kafedra.bll.dto.TeacherDto;
import com.electronic.kafedra.dal.entity.Discipline;
import com.electronic.kafedra.dal.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "disciplines", target = "disciplineIds",
            qualifiedByName = "disciplinesToIds")
    TeacherDto toDto(Teacher entity);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "disciplines", ignore = true)
    Teacher toEntity(TeacherDto dto);

    List<TeacherDto> toDtoList(List<Teacher> entities);

    @Named("disciplinesToIds")
    default List<Long> disciplinesToIds(List<Discipline> disciplines) {
        if (disciplines == null) return new ArrayList<>();
        return disciplines.stream()
                .map(Discipline::getId)
                .collect(Collectors.toList());
    }
}
