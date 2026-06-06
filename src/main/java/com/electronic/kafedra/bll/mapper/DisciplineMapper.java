package com.electronic.kafedra.bll.mapper;

import com.electronic.kafedra.bll.dto.DisciplineDto;
import com.electronic.kafedra.dal.entity.Discipline;
import com.electronic.kafedra.dal.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DisciplineMapper {

    @Mapping(source = "teachers", target = "teacherIds",
            qualifiedByName = "teachersToIds")
    DisciplineDto toDto(Discipline entity);

    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "grades", ignore = true)
    Discipline toEntity(DisciplineDto dto);

    List<DisciplineDto> toDtoList(List<Discipline> entities);

    @Named("teachersToIds")
    default List<Long> teachersToIds(List<Teacher> teachers) {
        if (teachers == null) return new ArrayList<>();
        return teachers.stream()
                .map(Teacher::getId)
                .collect(Collectors.toList());
    }
}
