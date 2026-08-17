package com.invisos.sims.academic.service;

import com.invisos.sims.academic.dto.request.SectionRequestDto;
import com.invisos.sims.academic.dto.response.SectionResponseDto;
import com.invisos.sims.academic.model.Sections;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface SectionsService {



    SectionResponseDto findById(UUID id);

    SectionResponseDto create(SectionRequestDto entity);

    SectionResponseDto assignClassTeacher(
            UUID sectionId,
            UUID teacherId
    );


    Sections getSectionEntity(UUID id);
    SectionResponseDto update(UUID id, SectionRequestDto entity);

    void delete(UUID id);

    List<SectionResponseDto> findAll(UUID academicYearId,UUID classId);
}
