package com.electronic.kafedra.dal.repository;

import com.electronic.kafedra.dal.entity.User;

import java.util.Optional;

public interface UserRepository extends GenericRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
