package com.invisos.sims.exam.service;

import com.invisos.sims.exam.model.Marks;

import java.util.List;
import java.util.UUID;

public interface MarksService {

    List<Marks> findAll();

    Marks findById(UUID id);

    Marks create(Marks entity);

    Marks update(UUID id, Marks entity);

    void delete(UUID id);
}
