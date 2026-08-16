package com.invisos.sims.attendance.service;

import com.invisos.sims.attendance.dto.ClassTimeTableBulkRequestDto;
import com.invisos.sims.attendance.dto.ClassTimeTableRequestDto;
import com.invisos.sims.attendance.dto.ClassTimeTableResponseDto;
import com.invisos.sims.attendance.model.ClassTimetable;

import java.util.List;
import java.util.UUID;

public interface ClassTimetableService {


    ClassTimetable getClassTimeTableEntity(UUID id);

    List<ClassTimeTableResponseDto> saveBulk(List<ClassTimeTableBulkRequestDto> requests);

    ClassTimeTableResponseDto findById(UUID id);

    ClassTimeTableResponseDto create(ClassTimeTableRequestDto entity);

    ClassTimeTableResponseDto update(UUID id, ClassTimeTableRequestDto entity);


    void delete(UUID id);

    List<ClassTimeTableResponseDto> findAll(UUID sectionId, UUID classId, UUID academicYearId);
}
