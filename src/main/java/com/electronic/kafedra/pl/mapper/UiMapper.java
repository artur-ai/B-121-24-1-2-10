package com.electronic.kafedra.pl.mapper;

import com.electronic.kafedra.bll.dto.*;
import com.electronic.kafedra.pl.model.request.*;
import com.electronic.kafedra.pl.model.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UiMapper {

    @Mapping(target = "role", ignore = true)
    AuthDto toDto(LoginRequest request);

    AuthDto toDto(RegisterRequest request);
    AuthResponse toResponse(TokenDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "disciplineIds", ignore = true)
    TeacherDto toDto(CreateTeacherRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "disciplineIds", ignore = true)
    TeacherDto toDto(UpdateTeacherRequest request);

    TeacherResponse toResponse(TeacherDto dto);
    List<TeacherResponse> toTeacherResponseList(List<TeacherDto> dtos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groupName", ignore = true)
    StudentDto toDto(CreateStudentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groupName", ignore = true)
    @Mapping(target = "userId", ignore = true)
    StudentDto toDto(UpdateStudentRequest request);

    StudentResponse toResponse(StudentDto dto);
    List<StudentResponse> toStudentResponseList(List<StudentDto> dtos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studentCount", ignore = true)
    GroupDto toDto(CreateGroupRequest request);

    GroupResponse toResponse(GroupDto dto);
    List<GroupResponse> toGroupResponseList(List<GroupDto> dtos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teacherIds", ignore = true)
    DisciplineDto toDto(CreateDisciplineRequest request);

    DisciplineResponse toResponse(DisciplineDto dto);
    List<DisciplineResponse> toDisciplineResponseList(List<DisciplineDto> dtos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studentFirstName", ignore = true)
    @Mapping(target = "studentLastName", ignore = true)
    @Mapping(target = "disciplineName", ignore = true)
    @Mapping(target = "teacherId", ignore = true)
    @Mapping(target = "gradedAt", ignore = true)
    GradeDto toDto(CreateGradeRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "studentFirstName", ignore = true)
    @Mapping(target = "studentLastName", ignore = true)
    @Mapping(target = "disciplineId", ignore = true)
    @Mapping(target = "disciplineName", ignore = true)
    @Mapping(target = "teacherId", ignore = true)
    @Mapping(target = "gradedAt", ignore = true)
    GradeDto toDto(UpdateGradeRequest request);

    GradeResponse toResponse(GradeDto dto);
    List<GradeResponse> toGradeResponseList(List<GradeDto> dtos);
}
