package com.electronic.kafedra.dal.repository.jpa;

import com.electronic.kafedra.dal.repository.GenericRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class JpaGenericRepository<T, ID> implements GenericRepository<T, ID> {

    protected final JpaRepository<T, ID> jpaRepository;

    protected JpaGenericRepository(JpaRepository<T, ID> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public T findById(ID id) {
        return jpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Entity not found with id: " + id
                ));
    }

    @Override
    public List<T> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public T save(T entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public void delete(T entity) {
        jpaRepository.delete(entity);
    }
}
