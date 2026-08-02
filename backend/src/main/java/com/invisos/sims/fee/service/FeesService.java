package com.invisos.sims.fee.service;

import com.invisos.sims.fee.dto.request.FeeRequestDto;
import com.invisos.sims.fee.model.Fees;

import java.util.List;
import java.util.UUID;

public interface FeesService {

    List<Fees> findAll();

    Fees findById(UUID id);

    Fees create(FeeRequestDto request);

    Fees update(UUID id, FeeRequestDto request);

    void delete(UUID id);
}
