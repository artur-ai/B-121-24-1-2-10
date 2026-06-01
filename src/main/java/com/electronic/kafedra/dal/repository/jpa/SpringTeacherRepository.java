package com.electronic.kafedra.dal.repository.jpa;

import com.electronic.kafedra.dal.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringTeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findByLastNameContainingIgnoreCase(String lastName);

    List<Teacher> findByDisciplinesId(Long disciplineId);

    Optional<Teacher> findByUserId(Long userId);
}
