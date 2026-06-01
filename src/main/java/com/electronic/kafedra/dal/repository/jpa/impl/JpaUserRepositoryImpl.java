package com.electronic.kafedra.dal.repository.jpa.impl;

import com.electronic.kafedra.dal.entity.User;
import com.electronic.kafedra.dal.repository.UserRepository;
import com.electronic.kafedra.dal.repository.jpa.JpaGenericRepository;
import com.electronic.kafedra.dal.repository.jpa.SpringUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUserRepositoryImpl
        extends JpaGenericRepository<User, Long>
        implements UserRepository {

    private final SpringUserRepository springRepo;

    public JpaUserRepositoryImpl(SpringUserRepository springRepo) {
        super(springRepo);
        this.springRepo = springRepo;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return springRepo.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return springRepo.existsByUsername(username);
    }
}
