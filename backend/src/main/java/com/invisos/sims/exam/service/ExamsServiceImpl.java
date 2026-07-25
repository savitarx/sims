package com.invisos.sims.exam.service;

import com.invisos.sims.exam.model.Exams;
import com.invisos.sims.exam.repository.ExamsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExamsServiceImpl implements ExamsService {

    private final ExamsRepository examsRepository;

    public ExamsServiceImpl(ExamsRepository examsRepository) {
        this.examsRepository = examsRepository;
    }

    @Override
    public List<Exams> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Exams findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Exams create(Exams entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Exams update(UUID id, Exams entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
