package com.electronic.kafedra.dal.repository.jpa;

import com.electronic.kafedra.dal.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringStudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByGroupId(Long groupId);

    List<Student> findByLastNameContainingIgnoreCase(String lastName);

    Optional<Student> findByUserId(Long userId);
}
