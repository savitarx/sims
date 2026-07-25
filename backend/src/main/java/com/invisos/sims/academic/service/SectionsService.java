package com.invisos.sims.academic.service;

import com.invisos.sims.academic.model.Sections;

import java.util.List;
import java.util.UUID;

public interface SectionsService {

    List<Sections> findAll();

    Sections findById(UUID id);

    Sections create(Sections entity);

    Sections update(UUID id, Sections entity);

    void delete(UUID id);
}
