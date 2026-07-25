package com.invisos.sims.academic.service;

import com.invisos.sims.academic.model.Classes;

import java.util.List;
import java.util.UUID;

public interface ClassesService {

    List<Classes> findAll();

    Classes findById(UUID id);

    Classes create(Classes entity);

    Classes update(UUID id, Classes entity);

    void delete(UUID id);
}
