package com.invisos.sims.student.service;

import com.invisos.sims.student.model.Parents;

import java.util.List;
import java.util.UUID;

public interface ParentsService {

    List<Parents> findAll();

    Parents findById(UUID id);

    Parents create(Parents entity);

    Parents update(UUID id, Parents entity);

    void delete(UUID id);
}
