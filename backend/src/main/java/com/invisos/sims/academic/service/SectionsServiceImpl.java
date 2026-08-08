package com.invisos.sims.academic.service;

import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.academic.repository.SectionsRepository;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SectionsServiceImpl implements SectionsService {

    private final SectionsRepository sectionsRepository;

    public SectionsServiceImpl(SectionsRepository sectionsRepository) {
        this.sectionsRepository = sectionsRepository;
    }

    @Override
    public List<Sections> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Sections findById(UUID id) {
        // TODO: implement
        return sectionsRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Section with given id not found"));
//        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Sections create(Sections entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Sections update(UUID id, Sections entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
