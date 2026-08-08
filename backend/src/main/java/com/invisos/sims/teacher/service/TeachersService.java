package com.invisos.sims.teacher.service;

import com.invisos.sims.teacher.dto.TeachersCountResponseDto;
import com.invisos.sims.teacher.dto.TeachersRequestDto;
import com.invisos.sims.teacher.dto.TeachersResponseDto;
import com.invisos.sims.teacher.model.Teachers;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.UUID;

public interface TeachersService {

    Teachers getActiveTeacherEntity(UUID id);

    List<TeachersResponseDto> findAll(UUID subjectId, Boolean active);

    TeachersResponseDto updateStatus(UUID id, boolean active);

    TeachersResponseDto restore(UUID id);

    TeachersResponseDto findById(UUID id);



    TeachersResponseDto create(TeachersRequestDto entity);

    TeachersResponseDto update(UUID id, TeachersRequestDto dto);

    void delete(UUID id);

    List<TeachersResponseDto> search(String query);


    TeachersCountResponseDto getCount();

    List<TeachersResponseDto> createBulk( List<TeachersRequestDto> teachers);



//    TeachersResponseDto uploadPhoto(UUID id, MultipartFile file);
}
