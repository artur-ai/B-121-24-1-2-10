package com.electronic.kafedra.bll.service.impl;

import com.electronic.kafedra.bll.dto.StudentDto;
import com.electronic.kafedra.bll.mapper.StudentMapper;
import com.electronic.kafedra.bll.service.StudentService;
import com.electronic.kafedra.dal.entity.Group;
import com.electronic.kafedra.dal.entity.Student;
import com.electronic.kafedra.dal.entity.User;
import com.electronic.kafedra.dal.uow.UnitOfWork;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final UnitOfWork uow;
    private final StudentMapper mapper;

    public StudentServiceImpl(UnitOfWork uow, StudentMapper mapper) {
        this.uow = uow;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getAll() {
        return mapper.toDtoList(uow.students().findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto getById(Long id) {
        return mapper.toDto(uow.students().findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getByGroup(Long groupId) {
        return mapper.toDtoList(uow.students().findByGroupId(groupId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> searchByLastName(String lastName) {
        return mapper.toDtoList(uow.students().findByLastName(lastName));
    }

    @Override
    public StudentDto create(StudentDto dto, Long userId) {
        Group group = uow.groups().findById(dto.getGroupId());
        Student student = mapper.toEntity(dto);
        student.setGroup(group);

        if (userId != null) {
            User user = uow.users().findById(userId);
            student.setUser(user);
        }

        return mapper.toDto(uow.students().save(student));
    }

    @Override
    public StudentDto update(Long id, StudentDto dto) {
        Student existing = uow.students().findById(id);
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());

        if (dto.getGroupId() != null) {
            Group group = uow.groups().findById(dto.getGroupId());
            existing.setGroup(group);
        }

        return mapper.toDto(uow.students().save(existing));
    }

    @Override
    public void delete(Long id) {
        Student student = uow.students().findById(id);
        uow.students().delete(student);
    }
}
