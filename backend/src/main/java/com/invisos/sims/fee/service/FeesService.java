package com.invisos.sims.fee.service;

import com.invisos.sims.fee.model.Fees;

import java.util.List;
import java.util.UUID;

public interface FeesService {

    List<Fees> findAll();

    Fees findById(UUID id);

    Fees create(Fees entity);

    Fees update(UUID id, Fees entity);

    void delete(UUID id);
}
