package com.invisos.sims.teacher.service;

import com.invisos.sims.teacher.model.Teachers;
import com.invisos.sims.teacher.repository.TeachersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TeachersServiceImpl implements TeachersService {

    private final TeachersRepository teachersRepository;

    public TeachersServiceImpl(TeachersRepository teachersRepository) {
        this.teachersRepository = teachersRepository;
    }

    @Override
    public List<Teachers> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Teachers findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Teachers create(Teachers entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Teachers update(UUID id, Teachers entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
