package com.electronic.kafedra.dal.repository;

import com.electronic.kafedra.dal.entity.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends GenericRepository<Teacher, Long> {
    List<Teacher> findByLastName(String lastName);

    List<Teacher> findByDisciplineId(Long disciplineId);

    Optional<Teacher> findByUserId(Long userId);
}
