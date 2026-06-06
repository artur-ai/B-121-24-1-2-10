package com.electronic.kafedra.bll.mapper;

import com.electronic.kafedra.bll.dto.GroupDto;
import com.electronic.kafedra.dal.entity.Group;
import com.electronic.kafedra.dal.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(source = "students", target = "studentCount",
            qualifiedByName = "studentsToCount")
    GroupDto toDto(Group entity);

    @Mapping(target = "students", ignore = true)
    Group toEntity(GroupDto dto);

    List<GroupDto> toDtoList(List<Group> entities);

    @Named("studentsToCount")
    default Integer studentsToCount(List<Student> students) {
        if (students == null) return 0;
        return students.size();
    }
}
