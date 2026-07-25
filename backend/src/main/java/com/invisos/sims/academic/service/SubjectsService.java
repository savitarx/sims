package com.invisos.sims.academic.service;

import com.invisos.sims.academic.model.Subjects;

import java.util.List;
import java.util.UUID;

public interface SubjectsService {

    List<Subjects> findAll();

    Subjects findById(UUID id);

    Subjects create(Subjects entity);

    Subjects update(UUID id, Subjects entity);

    void delete(UUID id);
}
