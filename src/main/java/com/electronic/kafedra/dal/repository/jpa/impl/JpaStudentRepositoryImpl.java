package com.electronic.kafedra.dal.repository.jpa.impl;

import com.electronic.kafedra.dal.entity.Student;
import com.electronic.kafedra.dal.repository.StudentRepository;
import com.electronic.kafedra.dal.repository.jpa.JpaGenericRepository;
import com.electronic.kafedra.dal.repository.jpa.SpringStudentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaStudentRepositoryImpl
        extends JpaGenericRepository<Student, Long>
        implements StudentRepository {

    private final SpringStudentRepository springRepo;

    public JpaStudentRepositoryImpl(SpringStudentRepository springRepo) {
        super(springRepo);
        this.springRepo = springRepo;
    }

    @Override
    public List<Student> findByGroupId(Long groupId) {
        return springRepo.findByGroupId(groupId);
    }

    @Override
    public List<Student> findByLastName(String lastName) {
        return springRepo.findByLastNameContainingIgnoreCase(lastName);
    }

    @Override
    public Optional<Student> findByUserId(Long userId) {
        return springRepo.findByUserId(userId);
    }
}
