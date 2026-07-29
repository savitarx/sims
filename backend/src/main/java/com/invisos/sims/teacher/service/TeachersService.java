package com.invisos.sims.teacher.service;

import com.invisos.sims.teacher.dto.TeachersRequestDto;
import com.invisos.sims.teacher.model.Teachers;

import java.util.List;
import java.util.UUID;

public interface TeachersService {

    List<Teachers> findAll();

    Teachers findById(UUID id);

    Teachers create(TeachersRequestDto entity);

    Teachers update(UUID id, Teachers entity);

    void delete(UUID id);
}
