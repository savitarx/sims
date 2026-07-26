package com.invisos.sims.student.service;

import com.invisos.sims.student.model.StudentEnrollment;
import com.invisos.sims.student.repository.StudentEnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {

    private final StudentEnrollmentRepository studentEnrollmentRepository;

    public StudentEnrollmentServiceImpl(StudentEnrollmentRepository studentEnrollmentRepository) {
        this.studentEnrollmentRepository = studentEnrollmentRepository;
    }

    @Override
    public List<StudentEnrollment> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public StudentEnrollment findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public StudentEnrollment create(StudentEnrollment entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public StudentEnrollment update(UUID id, StudentEnrollment entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
