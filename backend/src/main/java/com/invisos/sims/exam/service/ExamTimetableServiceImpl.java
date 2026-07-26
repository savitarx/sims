package com.invisos.sims.exam.service;

import com.invisos.sims.exam.model.ExamTimetable;
import com.invisos.sims.exam.repository.ExamTimetableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExamTimetableServiceImpl implements ExamTimetableService {

    private final ExamTimetableRepository examTimetableRepository;

    public ExamTimetableServiceImpl(ExamTimetableRepository examTimetableRepository) {
        this.examTimetableRepository = examTimetableRepository;
    }

    @Override
    public List<ExamTimetable> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public ExamTimetable findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public ExamTimetable create(ExamTimetable entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public ExamTimetable update(UUID id, ExamTimetable entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
