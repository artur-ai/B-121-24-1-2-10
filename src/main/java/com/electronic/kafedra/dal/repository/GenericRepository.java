package com.electronic.kafedra.dal.repository;

import java.util.List;

public interface GenericRepository<T, ID> {
    T findById(ID id);

    List<T> findAll();

    T save(T entity);

    void delete(T entity);
}
