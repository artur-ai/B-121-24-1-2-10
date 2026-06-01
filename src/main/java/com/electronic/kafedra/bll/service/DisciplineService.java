package com.electronic.kafedra.bll.service;

import com.electronic.kafedra.bll.dto.DisciplineDto;

import java.util.List;

public interface DisciplineService {
    List<DisciplineDto> getAll();

    DisciplineDto getById(Long id);

    List<DisciplineDto> searchByName(String name);

    List<DisciplineDto> getByTeacher(Long teacherId);

    DisciplineDto create(DisciplineDto dto);

    DisciplineDto update(Long id, DisciplineDto dto);

    void delete(Long id);
}
