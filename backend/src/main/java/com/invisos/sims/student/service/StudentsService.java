package com.invisos.sims.student.service;

import com.invisos.sims.student.model.Students;

import java.util.List;
import java.util.UUID;

public interface StudentsService {

    List<Students> findAll();

    Students findById(UUID id);

    Students create(Students entity);

    Students update(UUID id, Students entity);

    void delete(UUID id);
}
