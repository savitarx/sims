package com.invisos.sims.teacher.service;

import com.invisos.sims.teacher.model.TeacherAssignment;

import java.util.List;
import java.util.UUID;

public interface TeacherAssignmentService {

    List<TeacherAssignment> findAll();

    TeacherAssignment findById(UUID id);

    TeacherAssignment create(TeacherAssignment entity);

    TeacherAssignment update(UUID id, TeacherAssignment entity);

    void delete(UUID id);
}
