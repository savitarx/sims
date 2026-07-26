package com.invisos.sims.exam.service;

import com.invisos.sims.exam.model.Exams;

import java.util.List;
import java.util.UUID;

public interface ExamsService {

    List<Exams> findAll();

    Exams findById(UUID id);

    Exams create(Exams entity);

    Exams update(UUID id, Exams entity);

    void delete(UUID id);
}
