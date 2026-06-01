package com.electronic.kafedra.dal.repository.jpa;

import com.electronic.kafedra.dal.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringGradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentId(Long studentId);

    List<Grade> findByTeacherId(Long teacherId);

    List<Grade> findByDisciplineId(Long disciplineId);

    List<Grade> findByStudentIdAndDisciplineId(Long studentId, Long disciplineId);
}
