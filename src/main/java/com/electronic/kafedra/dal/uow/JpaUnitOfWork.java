package com.electronic.kafedra.dal.uow;

import com.electronic.kafedra.dal.repository.*;
import org.springframework.stereotype.Component;

@Component
public class JpaUnitOfWork implements UnitOfWork {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final DisciplineRepository disciplineRepository;
    private final GradeRepository gradeRepository;

    public JpaUnitOfWork(
            UserRepository userRepository,
            TeacherRepository teacherRepository,
            StudentRepository studentRepository,
            GroupRepository groupRepository,
            DisciplineRepository disciplineRepository,
            GradeRepository gradeRepository) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.disciplineRepository = disciplineRepository;
        this.gradeRepository = gradeRepository;
    }

    @Override
    public UserRepository users() {
        return userRepository;
    }

    @Override
    public TeacherRepository teachers() {
        return teacherRepository;
    }

    @Override
    public StudentRepository students() {
        return studentRepository;
    }

    @Override
    public GroupRepository groups() {
        return groupRepository;
    }

    @Override
    public DisciplineRepository disciplines() {
        return disciplineRepository;
    }

    @Override
    public GradeRepository grades() {
        return gradeRepository;
    }
}
