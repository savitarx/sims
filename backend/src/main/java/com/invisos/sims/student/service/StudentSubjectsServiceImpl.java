package com.invisos.sims.student.service;

import com.invisos.sims.student.model.StudentSubjects;
import com.invisos.sims.student.repository.StudentSubjectsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StudentSubjectsServiceImpl implements StudentSubjectsService {

    private final StudentSubjectsRepository studentSubjectsRepository;

    public StudentSubjectsServiceImpl(StudentSubjectsRepository studentSubjectsRepository) {
        this.studentSubjectsRepository = studentSubjectsRepository;
    }

    @Override
    public List<StudentSubjects> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public StudentSubjects findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public StudentSubjects create(StudentSubjects entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public StudentSubjects update(UUID id, StudentSubjects entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
