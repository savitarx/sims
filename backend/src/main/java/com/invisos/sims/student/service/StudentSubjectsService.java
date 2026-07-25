package com.invisos.sims.student.service;

import com.invisos.sims.student.model.StudentSubjects;

import java.util.List;
import java.util.UUID;

public interface StudentSubjectsService {

    List<StudentSubjects> findAll();

    StudentSubjects findById(UUID id);

    StudentSubjects create(StudentSubjects entity);

    StudentSubjects update(UUID id, StudentSubjects entity);

    void delete(UUID id);
}
