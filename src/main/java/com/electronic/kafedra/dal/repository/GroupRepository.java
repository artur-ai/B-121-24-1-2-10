package com.electronic.kafedra.dal.repository;

import com.electronic.kafedra.dal.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends GenericRepository<Group, Long> {
    Optional<Group> findByName(String name);

    List<Group> findByYear(Integer year);
}
