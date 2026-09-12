package com.invisos.sims.fee.service;

import com.invisos.sims.fee.dto.request.FeeRequestDto;
import com.invisos.sims.fee.model.Fees;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FeesService {

    Page<Fees> findAll(UUID classId, UUID academicYearId, Pageable pageable);

    Fees findById(UUID id);

    Fees create(FeeRequestDto request);

    Fees update(UUID id, FeeRequestDto request);

    void delete(UUID id);
}
