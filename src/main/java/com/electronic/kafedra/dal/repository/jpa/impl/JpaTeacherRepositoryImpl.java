package com.electronic.kafedra.dal.repository.jpa.impl;

import com.electronic.kafedra.dal.entity.Teacher;
import com.electronic.kafedra.dal.repository.TeacherRepository;
import com.electronic.kafedra.dal.repository.jpa.JpaGenericRepository;
import com.electronic.kafedra.dal.repository.jpa.SpringTeacherRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaTeacherRepositoryImpl
        extends JpaGenericRepository<Teacher, Long>
        implements TeacherRepository {

    private final SpringTeacherRepository springRepo;

    public JpaTeacherRepositoryImpl(SpringTeacherRepository springRepo) {
        super(springRepo);
        this.springRepo = springRepo;
    }

    @Override
    public List<Teacher> findByLastName(String lastName) {
        return springRepo.findByLastNameContainingIgnoreCase(lastName);
    }

    @Override
    public List<Teacher> findByDisciplineId(Long disciplineId) {
        return springRepo.findByDisciplinesId(disciplineId);
    }

    @Override
    public Optional<Teacher> findByUserId(Long userId) {
        return springRepo.findByUserId(userId);
    }
}
