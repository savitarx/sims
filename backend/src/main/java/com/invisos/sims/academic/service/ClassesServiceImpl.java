package com.invisos.sims.academic.service;

import com.invisos.sims.academic.model.Classes;
import com.invisos.sims.academic.repository.ClassesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClassesServiceImpl implements ClassesService {

    private final ClassesRepository classesRepository;

    public ClassesServiceImpl(ClassesRepository classesRepository) {
        this.classesRepository = classesRepository;
    }

    @Override
    public List<Classes> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Classes findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Classes create(Classes entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Classes update(UUID id, Classes entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
