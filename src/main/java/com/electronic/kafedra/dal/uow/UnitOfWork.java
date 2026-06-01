package com.electronic.kafedra.dal.uow;

import com.electronic.kafedra.dal.repository.*;

public interface UnitOfWork {
    UserRepository users();

    TeacherRepository teachers();

    StudentRepository students();

    GroupRepository groups();

    DisciplineRepository disciplines();

    GradeRepository grades();
}