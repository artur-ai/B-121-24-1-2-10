package com.electronic.kafedra.bll.service.impl;

import com.electronic.kafedra.bll.dto.GroupDto;
import com.electronic.kafedra.bll.mapper.GroupMapper;
import com.electronic.kafedra.bll.service.GroupService;
import com.electronic.kafedra.common.exception.AlreadyExistsException;
import com.electronic.kafedra.dal.entity.Group;
import com.electronic.kafedra.dal.uow.UnitOfWork;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final UnitOfWork uow;
    private final GroupMapper mapper;

    public GroupServiceImpl(UnitOfWork uow, GroupMapper mapper) {
        this.uow = uow;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupDto> getAll() {
        return mapper.toDtoList(uow.groups().findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public GroupDto getById(Long id) {
        return mapper.toDto(uow.groups().findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupDto> getByYear(Integer year) {
        return mapper.toDtoList(uow.groups().findByYear(year));
    }

    @Override
    public GroupDto create(GroupDto dto) {
        uow.groups().findByName(dto.getName()).ifPresent(g -> {
            throw new AlreadyExistsException(
                    "Group already exists: " + dto.getName()
            );
        });

        Group group = mapper.toEntity(dto);
        return mapper.toDto(uow.groups().save(group));
    }

    @Override
    public GroupDto update(Long id, GroupDto dto) {
        Group existing = uow.groups().findById(id);
        existing.setName(dto.getName());
        existing.setYear(dto.getYear());
        return mapper.toDto(uow.groups().save(existing));
    }

    @Override
    public void delete(Long id) {
        Group group = uow.groups().findById(id);
        uow.groups().delete(group);
    }
}
