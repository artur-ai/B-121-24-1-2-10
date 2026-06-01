package com.electronic.kafedra.dal.repository.jpa.impl;

import com.electronic.kafedra.dal.entity.Group;
import com.electronic.kafedra.dal.repository.GroupRepository;
import com.electronic.kafedra.dal.repository.jpa.JpaGenericRepository;
import com.electronic.kafedra.dal.repository.jpa.SpringGroupRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaGroupRepositoryImpl
        extends JpaGenericRepository<Group, Long>
        implements GroupRepository {

    private final SpringGroupRepository springRepo;

    public JpaGroupRepositoryImpl(SpringGroupRepository springRepo) {
        super(springRepo);
        this.springRepo = springRepo;
    }

    @Override
    public Optional<Group> findByName(String name) {
        return springRepo.findByName(name);
    }

    @Override
    public List<Group> findByYear(Integer year) {
        return springRepo.findByYear(year);
    }
}
