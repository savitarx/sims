package com.invisos.sims.exam.service.impl;

import com.invisos.sims.exam.model.ExamSubjects;
import com.invisos.sims.exam.repository.ExamSubjectsRepository;
import com.invisos.sims.exam.service.ExamSubjectsService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExamSubjectsServiceImpl implements ExamSubjectsService {

    private final ExamSubjectsRepository examSubjectsRepository;

    public ExamSubjectsServiceImpl(ExamSubjectsRepository examSubjectsRepository) {
        this.examSubjectsRepository = examSubjectsRepository;
    }

    @Override
    public List<ExamSubjects> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public ExamSubjects findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public ExamSubjects create(ExamSubjects entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public ExamSubjects update(UUID id, ExamSubjects entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
