package com.electronic.kafedra.dal.repository.jpa;

import com.electronic.kafedra.dal.entity.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDisciplineRepository extends JpaRepository<Discipline, Long> {
    List<Discipline> findByNameContainingIgnoreCase(String name);

    List<Discipline> findByTeachersId(Long teacherId);
}