package com.electronic.kafedra.dal.repository.jpa;

import com.electronic.kafedra.dal.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringGroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByName(String name);

    List<Group> findByYear(Integer year);
}
