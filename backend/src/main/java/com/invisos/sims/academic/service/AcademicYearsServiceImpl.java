package com.invisos.sims.academic.service;

import com.invisos.sims.academic.model.AcademicYears;
import com.invisos.sims.academic.repository.AcademicYearsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AcademicYearsServiceImpl implements AcademicYearsService {

    private final AcademicYearsRepository academicYearsRepository;

    public AcademicYearsServiceImpl(AcademicYearsRepository academicYearsRepository) {
        this.academicYearsRepository = academicYearsRepository;
    }

    @Override
    public List<AcademicYears> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public AcademicYears findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public AcademicYears create(AcademicYears entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public AcademicYears update(UUID id, AcademicYears entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }
}
