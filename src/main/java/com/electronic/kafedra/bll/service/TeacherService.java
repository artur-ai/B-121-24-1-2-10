package com.electronic.kafedra.bll.service;

import com.electronic.kafedra.bll.dto.TeacherDto;

import java.util.List;

public interface TeacherService {
    List<TeacherDto> getAll();

    TeacherDto getById(Long id);

    List<TeacherDto> searchByLastName(String lastName);

    List<TeacherDto> getByDiscipline(Long disciplineId);

    TeacherDto create(TeacherDto dto, Long userId);

    TeacherDto update(Long id, TeacherDto dto);

    void delete(Long id);

    TeacherDto assignDiscipline(Long teacherId, Long disciplineId);

    TeacherDto removeDiscipline(Long teacherId, Long disciplineId);
}
