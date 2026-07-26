package com.invisos.sims.academic.service;

import com.invisos.sims.academic.model.AcademicYears;

import java.util.List;
import java.util.UUID;

public interface AcademicYearsService {

    List<AcademicYears> findAll();

    AcademicYears findById(UUID id);

    AcademicYears create(AcademicYears entity);

    AcademicYears update(UUID id, AcademicYears entity);

    void delete(UUID id);
}
