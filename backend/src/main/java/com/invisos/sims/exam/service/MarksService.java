package com.invisos.sims.exam.service;

import com.invisos.sims.exam.dto.request.MarkRequestDto;
import com.invisos.sims.exam.model.Marks;

import java.util.List;
import java.util.UUID;

public interface MarksService {

    List<Marks> findAll();

    Marks findById(UUID id);

    List<Marks> findByExamSubjectId(UUID examSubjectId);

    List<Marks> findByEnrollmentId(UUID enrollmentId);

    Marks create(MarkRequestDto request);

    Marks update(UUID id, MarkRequestDto request);

    void delete(UUID id);

}
