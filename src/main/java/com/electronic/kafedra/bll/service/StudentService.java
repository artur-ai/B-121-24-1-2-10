package com.electronic.kafedra.bll.service;

import com.electronic.kafedra.bll.dto.StudentDto;

import java.util.List;

public interface StudentService {
    List<StudentDto> getAll();

    StudentDto getById(Long id);

    List<StudentDto> getByGroup(Long groupId);

    List<StudentDto> searchByLastName(String lastName);

    StudentDto create(StudentDto dto, Long userId);

    StudentDto update(Long id, StudentDto dto);

    void delete(Long id);
}
