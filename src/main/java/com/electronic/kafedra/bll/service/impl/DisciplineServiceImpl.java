package com.electronic.kafedra.bll.service.impl;

import com.electronic.kafedra.bll.dto.DisciplineDto;
import com.electronic.kafedra.bll.mapper.DisciplineMapper;
import com.electronic.kafedra.bll.service.DisciplineService;
import com.electronic.kafedra.dal.entity.Discipline;
import com.electronic.kafedra.dal.uow.UnitOfWork;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DisciplineServiceImpl implements DisciplineService {

    private final UnitOfWork uow;
    private final DisciplineMapper mapper;

    public DisciplineServiceImpl(UnitOfWork uow, DisciplineMapper mapper) {
        this.uow = uow;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisciplineDto> getAll() {
        return mapper.toDtoList(uow.disciplines().findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public DisciplineDto getById(Long id) {
        return mapper.toDto(uow.disciplines().findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisciplineDto> searchByName(String name) {
        return mapper.toDtoList(uow.disciplines().findByName(name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisciplineDto> getByTeacher(Long teacherId) {
        return mapper.toDtoList(uow.disciplines().findByTeacherId(teacherId));
    }

    @Override
    public DisciplineDto create(DisciplineDto dto) {
        Discipline discipline = mapper.toEntity(dto);
        return mapper.toDto(uow.disciplines().save(discipline));
    }

    @Override
    public DisciplineDto update(Long id, DisciplineDto dto) {
        Discipline existing = uow.disciplines().findById(id);
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        return mapper.toDto(uow.disciplines().save(existing));
    }

    @Override
    public void delete(Long id) {
        Discipline discipline = uow.disciplines().findById(id);
        uow.disciplines().delete(discipline);
    }
}
