package com.invisos.sims.student.service;

import com.invisos.sims.student.model.StudentEnrollment;

import java.util.List;
import java.util.UUID;

public interface StudentEnrollmentService {

    List<StudentEnrollment> findAll();

    StudentEnrollment findById(UUID id);

    StudentEnrollment create(StudentEnrollment entity);

    StudentEnrollment update(UUID id, StudentEnrollment entity);

    void delete(UUID id);
}
