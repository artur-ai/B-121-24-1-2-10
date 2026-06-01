package com.electronic.kafedra.dal.repository.jpa.impl;

import com.electronic.kafedra.dal.entity.Grade;
import com.electronic.kafedra.dal.repository.GradeRepository;
import com.electronic.kafedra.dal.repository.jpa.JpaGenericRepository;
import com.electronic.kafedra.dal.repository.jpa.SpringGradeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaGradeRepositoryImpl
        extends JpaGenericRepository<Grade, Long>
        implements GradeRepository {

    private final SpringGradeRepository springRepo;

    public JpaGradeRepositoryImpl(SpringGradeRepository springRepo) {
        super(springRepo);
        this.springRepo = springRepo;
    }

    @Override
    public List<Grade> findByStudentId(Long studentId) {
        return springRepo.findByStudentId(studentId);
    }

    @Override
    public List<Grade> findByTeacherId(Long teacherId) {
        return springRepo.findByTeacherId(teacherId);
    }

    @Override
    public List<Grade> findByDisciplineId(Long disciplineId) {
        return springRepo.findByDisciplineId(disciplineId);
    }

    @Override
    public List<Grade> findByStudentIdAndDisciplineId(Long studentId, Long disciplineId) {
        return springRepo.findByStudentIdAndDisciplineId(studentId, disciplineId);
    }
}
