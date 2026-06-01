package com.electronic.kafedra.bll.service;

import com.electronic.kafedra.bll.dto.GradeDto;

import java.util.List;

public interface GradeService {
    List<GradeDto> getByStudent(Long studentId);

    List<GradeDto> getByTeacher(Long teacherId);

    List<GradeDto> getByDiscipline(Long disciplineId);

    List<GradeDto> getByStudentAndDiscipline(Long studentId, Long disciplineId);

    GradeDto addGrade(GradeDto dto, Long currentUserId);

    GradeDto updateGrade(Long id, GradeDto dto, Long currentUserId);

    void deleteGrade(Long id, Long currentUserId);
}
