package com.electronic.kafedra.dal.repository;

import com.electronic.kafedra.dal.entity.Discipline;

import java.util.List;

public interface DisciplineRepository extends GenericRepository<Discipline, Long> {
    List<Discipline> findByName(String name);

    List<Discipline> findByTeacherId(Long teacherId);
}
