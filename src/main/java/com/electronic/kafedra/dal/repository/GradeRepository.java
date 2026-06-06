package com.electronic.kafedra.dal.repository;

import com.electronic.kafedra.dal.entity.Grade;

import java.util.List;

public interface GradeRepository extends GenericRepository<Grade, Long> {
    List<Grade> findByStudentId(Long studentId);

    List<Grade> findByTeacherId(Long teacherId);

    List<Grade> findByDisciplineId(Long disciplineId);

    List<Grade> findByStudentIdAndDisciplineId(Long studentId, Long disciplineId);
}
