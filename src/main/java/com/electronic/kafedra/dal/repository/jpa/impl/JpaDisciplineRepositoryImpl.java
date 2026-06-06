package com.electronic.kafedra.dal.repository.jpa.impl;

import com.electronic.kafedra.dal.entity.Discipline;
import com.electronic.kafedra.dal.repository.DisciplineRepository;
import com.electronic.kafedra.dal.repository.jpa.JpaGenericRepository;
import com.electronic.kafedra.dal.repository.jpa.SpringDisciplineRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaDisciplineRepositoryImpl
        extends JpaGenericRepository<Discipline, Long>
        implements DisciplineRepository {

    private final SpringDisciplineRepository springRepo;

    public JpaDisciplineRepositoryImpl(SpringDisciplineRepository springRepo) {
        super(springRepo);
        this.springRepo = springRepo;
    }

    @Override
    public List<Discipline> findByName(String name) {
        return springRepo.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Discipline> findByTeacherId(Long teacherId) {
        return springRepo.findByTeachersId(teacherId);
    }
}
