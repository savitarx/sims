package com.invisos.sims.teacher.service;

import com.invisos.sims.teacher.model.TeacherAssignment;
import com.invisos.sims.teacher.repository.TeacherAssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TeacherAssignmentServiceImpl implements TeacherAssignmentService {

    private final TeacherAssignmentRepository teacherAssignmentRepository;

    public TeacherAssignmentServiceImpl(TeacherAssignmentRepository teacherAssignmentRepository) {
        this.teacherAssignmentRepository = teacherAssignmentRepository;
    }

    @Override
    public List<TeacherAssignment> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public TeacherAssignment findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public TeacherAssignment create(TeacherAssignment entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public TeacherAssignment update(UUID id, TeacherAssignment entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
