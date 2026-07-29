package com.invisos.sims.exam.service;

import com.invisos.sims.exam.dto.request.ExamRequestDto;
import com.invisos.sims.exam.model.Exams;

import java.util.List;
import java.util.UUID;

public interface ExamsService {

    List<Exams> findAll();

    Exams findById(UUID id);

    Exams create(ExamRequestDto request);

    Exams update(UUID id, ExamRequestDto request);

    Exams publish(UUID id);

    void delete(UUID id);
    
}
