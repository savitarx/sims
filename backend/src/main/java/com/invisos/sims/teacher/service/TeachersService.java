package com.invisos.sims.teacher.service;

import com.invisos.sims.teacher.dto.TeachersRequestDto;
import com.invisos.sims.teacher.dto.TeachersResponseDto;


import java.util.List;
import java.util.UUID;

public interface TeachersService {

    List<TeachersResponseDto> findAll();

    TeachersResponseDto findById(UUID id);

    TeachersResponseDto create(TeachersRequestDto entity);

    TeachersResponseDto update(UUID id, TeachersRequestDto dto);

    void delete(UUID id);
}
