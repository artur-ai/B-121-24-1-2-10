package com.electronic.kafedra.bll.service.impl;

import com.electronic.kafedra.bll.dto.GradeDto;
import com.electronic.kafedra.bll.mapper.GradeMapper;
import com.electronic.kafedra.bll.service.GradeService;
import com.electronic.kafedra.common.exception.EntityNotFoundException;
import com.electronic.kafedra.dal.entity.Discipline;
import com.electronic.kafedra.dal.entity.Grade;
import com.electronic.kafedra.dal.entity.Student;
import com.electronic.kafedra.dal.entity.Teacher;
import com.electronic.kafedra.dal.uow.UnitOfWork;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class GradeServiceImpl implements GradeService {

    private final UnitOfWork uow;
    private final GradeMapper mapper;

    public GradeServiceImpl(UnitOfWork uow, GradeMapper mapper) {
        this.uow = uow;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeDto> getByStudent(Long studentId) {
        return mapper.toDtoList(uow.grades().findByStudentId(studentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeDto> getByTeacher(Long teacherId) {
        return mapper.toDtoList(uow.grades().findByTeacherId(teacherId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeDto> getByDiscipline(Long disciplineId) {
        return mapper.toDtoList(uow.grades().findByDisciplineId(disciplineId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeDto> getByStudentAndDiscipline(Long studentId, Long disciplineId) {
        return mapper.toDtoList(
                uow.grades().findByStudentIdAndDisciplineId(studentId, disciplineId)
        );
    }

    @Override
    public GradeDto addGrade(GradeDto dto, Long currentUserId) {
        Teacher teacher = uow.teachers().findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Teacher not found for user: " + currentUserId
                ));

        if (!teacher.getDisciplines().stream()
                .anyMatch(d -> d.getId().equals(dto.getDisciplineId()))) {
            throw new com.electronic.kafedra.common.exception
                    .AccessDeniedException(
                    "Teacher does not teach this discipline"
            );
        }

        Student student = uow.students().findById(dto.getStudentId());
        Discipline discipline = uow.disciplines().findById(dto.getDisciplineId());

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setDiscipline(discipline);
        grade.setTeacher(teacher);
        grade.setValue(dto.getValue());
        grade.setGradedAt(LocalDateTime.now());
        grade.setComment(dto.getComment());

        return mapper.toDto(uow.grades().save(grade));
    }

    @Override
    public GradeDto updateGrade(Long id, GradeDto dto, Long currentUserId) {
        Grade grade = uow.grades().findById(id);

        Teacher teacher = uow.teachers().findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Teacher not found for user: " + currentUserId
                ));

        if (!grade.getTeacher().getId().equals(teacher.getId())) {
            throw new com.electronic.kafedra.common.exception
                    .AccessDeniedException(
                    "You can only update your own grades"
            );
        }

        grade.setValue(dto.getValue());
        grade.setComment(dto.getComment());
        return mapper.toDto(uow.grades().save(grade));
    }

    @Override
    public void deleteGrade(Long id, Long currentUserId) {
        Grade grade = uow.grades().findById(id);

        Teacher teacher = uow.teachers().findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Teacher not found for user: " + currentUserId
                ));

        if (!grade.getTeacher().getId().equals(teacher.getId())) {
            throw new com.electronic.kafedra.common.exception
                    .AccessDeniedException(
                    "You can only delete your own grades"
            );
        }

        uow.grades().delete(grade);
    }
}
