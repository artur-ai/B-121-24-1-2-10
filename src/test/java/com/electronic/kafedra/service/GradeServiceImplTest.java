package com.electronic.kafedra.service;

import com.electronic.kafedra.bll.dto.GradeDto;
import com.electronic.kafedra.bll.mapper.GradeMapper;
import com.electronic.kafedra.bll.service.impl.GradeServiceImpl;
import com.electronic.kafedra.common.exception.EntityNotFoundException;
import com.electronic.kafedra.dal.entity.Discipline;
import com.electronic.kafedra.dal.entity.Grade;
import com.electronic.kafedra.dal.entity.Student;
import com.electronic.kafedra.dal.entity.Teacher;
import com.electronic.kafedra.dal.repository.DisciplineRepository;
import com.electronic.kafedra.dal.repository.GradeRepository;
import com.electronic.kafedra.dal.repository.StudentRepository;
import com.electronic.kafedra.dal.repository.TeacherRepository;
import com.electronic.kafedra.dal.uow.UnitOfWork;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GradeServiceImplTest {

    @Mock
    private UnitOfWork uow;

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DisciplineRepository disciplineRepository;

    @Mock
    private GradeMapper gradeMapper;

    @InjectMocks
    private GradeServiceImpl gradeService;

    private Teacher teacher;
    private Student student;
    private Discipline discipline;
    private Grade grade;
    private GradeDto gradeDto;

    @BeforeEach
    void setUp() {
        discipline = new Discipline();
        discipline.setId(1L);
        discipline.setName("Mathematics");

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setDisciplines(new ArrayList<>(List.of(discipline)));

        student = new Student();
        student.setId(1L);
        student.setFirstName("Jane");
        student.setLastName("Smith");

        grade = new Grade();
        grade.setId(1L);
        grade.setTeacher(teacher);
        grade.setStudent(student);
        grade.setDiscipline(discipline);
        grade.setValue(90);
        grade.setGradedAt(LocalDateTime.now());

        gradeDto = new GradeDto();
        gradeDto.setStudentId(1L);
        gradeDto.setDisciplineId(1L);
        gradeDto.setValue(90);

        when(uow.grades()).thenReturn(gradeRepository);
        when(uow.teachers()).thenReturn(teacherRepository);
        when(uow.students()).thenReturn(studentRepository);
        when(uow.disciplines()).thenReturn(disciplineRepository);
    }

    @Test
    void addGrade_whenTeacherTeachesDiscipline_shouldSaveGrade() {
        when(teacherRepository.findByUserId(1L))
                .thenReturn(Optional.of(teacher));
        when(studentRepository.findById(1L))
                .thenReturn(student);
        when(disciplineRepository.findById(1L))
                .thenReturn(discipline);
        when(gradeRepository.save(any(Grade.class)))
                .thenReturn(grade);
        when(gradeMapper.toDto(grade))
                .thenReturn(gradeDto);

        GradeDto result = gradeService.addGrade(gradeDto, 1L);

        assertNotNull(result);
        assertEquals(90, result.getValue());
        verify(gradeRepository).save(any(Grade.class));
    }

    @Test
    void addGrade_whenTeacherNotFound_shouldThrowEntityNotFoundException() {
        when(teacherRepository.findByUserId(99L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> gradeService.addGrade(gradeDto, 99L)
        );
        verify(gradeRepository, never()).save(any());
    }

    @Test
    void addGrade_whenTeacherDoesNotTeachDiscipline_shouldThrowAccessDeniedException() {
        Discipline otherDiscipline = new Discipline();
        otherDiscipline.setId(99L);

        Teacher teacherWithoutDiscipline = new Teacher();
        teacherWithoutDiscipline.setId(2L);
        teacherWithoutDiscipline.setDisciplines(
                new ArrayList<>(List.of(otherDiscipline))
        );

        when(teacherRepository.findByUserId(2L))
                .thenReturn(Optional.of(teacherWithoutDiscipline));

        GradeDto dtoWithWrongDiscipline = new GradeDto();
        dtoWithWrongDiscipline.setStudentId(1L);
        dtoWithWrongDiscipline.setDisciplineId(1L);
        dtoWithWrongDiscipline.setValue(85);

        assertThrows(
                com.electronic.kafedra.common.exception.AccessDeniedException.class,
                () -> gradeService.addGrade(dtoWithWrongDiscipline, 2L)
        );
    }


    @Test
    void updateGrade_whenTeacherOwnsGrade_shouldUpdateGrade() {
        GradeDto updateDto = new GradeDto();
        updateDto.setValue(95);
        updateDto.setComment("Excellent");

        Grade updatedGrade = new Grade();
        updatedGrade.setId(1L);
        updatedGrade.setValue(95);
        updatedGrade.setComment("Excellent");
        updatedGrade.setTeacher(teacher);

        GradeDto updatedDto = new GradeDto();
        updatedDto.setValue(95);

        when(gradeRepository.findById(1L)).thenReturn(grade);
        when(teacherRepository.findByUserId(1L))
                .thenReturn(Optional.of(teacher));
        when(gradeRepository.save(any(Grade.class)))
                .thenReturn(updatedGrade);
        when(gradeMapper.toDto(updatedGrade)).thenReturn(updatedDto);

        GradeDto result = gradeService.updateGrade(1L, updateDto, 1L);

        assertNotNull(result);
        assertEquals(95, result.getValue());
        verify(gradeRepository).save(any(Grade.class));
    }

    @Test
    void updateGrade_whenTeacherDoesNotOwnGrade_shouldThrowAccessDeniedException() {
        Teacher anotherTeacher = new Teacher();
        anotherTeacher.setId(2L);

        when(gradeRepository.findById(1L)).thenReturn(grade);
        when(teacherRepository.findByUserId(2L))
                .thenReturn(Optional.of(anotherTeacher));

        GradeDto updateDto = new GradeDto();
        updateDto.setValue(70);

        assertThrows(
                com.electronic.kafedra.common.exception.AccessDeniedException.class,
                () -> gradeService.updateGrade(1L, updateDto, 2L)
        );
        verify(gradeRepository, never()).save(any());
    }

    @Test
    void deleteGrade_whenTeacherOwnsGrade_shouldDelete() {
        when(gradeRepository.findById(1L)).thenReturn(grade);
        when(teacherRepository.findByUserId(1L))
                .thenReturn(Optional.of(teacher));

        gradeService.deleteGrade(1L, 1L);

        verify(gradeRepository).delete(grade);
    }

    @Test
    void deleteGrade_whenTeacherDoesNotOwnGrade_shouldThrowAccessDeniedException() {
        Teacher anotherTeacher = new Teacher();
        anotherTeacher.setId(2L);

        when(gradeRepository.findById(1L)).thenReturn(grade);
        when(teacherRepository.findByUserId(2L))
                .thenReturn(Optional.of(anotherTeacher));

        assertThrows(
                com.electronic.kafedra.common.exception.AccessDeniedException.class,
                () -> gradeService.deleteGrade(1L, 2L)
        );
        verify(gradeRepository, never()).delete(any());
    }

    @Test
    void getByStudent_shouldReturnGrades() {
        List<Grade> grades = List.of(grade);
        List<GradeDto> gradeDtos = List.of(gradeDto);

        when(gradeRepository.findByStudentId(1L)).thenReturn(grades);
        when(gradeMapper.toDtoList(grades)).thenReturn(gradeDtos);

        List<GradeDto> result = gradeService.getByStudent(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(gradeRepository).findByStudentId(1L);
    }
}
