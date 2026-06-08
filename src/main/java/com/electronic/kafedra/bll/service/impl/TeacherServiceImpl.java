package com.electronic.kafedra.bll.service.impl;

import com.electronic.kafedra.bll.dto.TeacherDto;
import com.electronic.kafedra.bll.mapper.TeacherMapper;
import com.electronic.kafedra.bll.service.TeacherService;
import com.electronic.kafedra.dal.entity.Discipline;
import com.electronic.kafedra.dal.entity.Teacher;
import com.electronic.kafedra.dal.entity.User;
import com.electronic.kafedra.dal.uow.UnitOfWork;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final UnitOfWork uow;
    private final TeacherMapper mapper;

    public TeacherServiceImpl(UnitOfWork uow, TeacherMapper mapper) {
        this.uow = uow;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDto> getAll() {
        return mapper.toDtoList(uow.teachers().findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherDto getById(Long id) {
        return mapper.toDto(uow.teachers().findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDto> searchByLastName(String lastName) {
        return mapper.toDtoList(uow.teachers().findByLastName(lastName));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDto> getByDiscipline(Long disciplineId) {
        return mapper.toDtoList(uow.teachers().findByDisciplineId(disciplineId));
    }

    @Override
    public TeacherDto create(TeacherDto dto, Long userId) {
        Teacher teacher = mapper.toEntity(dto);
        if (userId != null) {
            User user = uow.users().findById(userId);
            teacher.setUser(user);
        }
        return mapper.toDto(uow.teachers().save(teacher));
    }

    @Override
    public TeacherDto update(Long id, TeacherDto dto) {
        Teacher existing = uow.teachers().findById(id);
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPosition(dto.getPosition());

        if (dto.getUserId() != null) {
            User user = uow.users().findById(dto.getUserId());
            existing.setUser(user);
        } else {
            existing.setUser(null);
        }

        return mapper.toDto(uow.teachers().save(existing));
    }

    @Override
    public void delete(Long id) {
        Teacher teacher = uow.teachers().findById(id);
        uow.teachers().delete(teacher);
    }

    @Override
    public TeacherDto assignDiscipline(Long teacherId, Long disciplineId) {
        Teacher teacher = uow.teachers().findById(teacherId);
        Discipline discipline = uow.disciplines().findById(disciplineId);

        if (!discipline.getTeachers().contains(teacher)) {
            discipline.getTeachers().add(teacher);
        }

        uow.disciplines().save(discipline);
        return mapper.toDto(uow.teachers().findById(teacherId));
    }

    @Override
    public TeacherDto removeDiscipline(Long teacherId, Long disciplineId) {
        Teacher teacher = uow.teachers().findById(teacherId);
        Discipline discipline = uow.disciplines().findById(disciplineId);
        discipline.getTeachers().remove(teacher);
        uow.disciplines().save(discipline);
        return mapper.toDto(uow.teachers().findById(teacherId));
    }
}
