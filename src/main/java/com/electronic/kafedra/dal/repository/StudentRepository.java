package com.electronic.kafedra.dal.repository;

import com.electronic.kafedra.dal.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends GenericRepository<Student, Long> {
    List<Student> findByGroupId(Long groupId);

    List<Student> findByLastName(String lastName);

    Optional<Student> findByUserId(Long userId);
}
