package com.invisos.sims.student.service;

import com.invisos.sims.student.model.Parents;
import com.invisos.sims.student.repository.ParentsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParentsServiceImpl implements ParentsService {

    private final ParentsRepository parentsRepository;

    public ParentsServiceImpl(ParentsRepository parentsRepository) {
        this.parentsRepository = parentsRepository;
    }

    @Override
    public List<Parents> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Parents findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Parents create(Parents entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Parents update(UUID id, Parents entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
