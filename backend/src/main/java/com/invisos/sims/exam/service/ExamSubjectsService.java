package com.invisos.sims.exam.service;

import com.invisos.sims.exam.dto.request.ExamSubjectRequestDto;
import com.invisos.sims.exam.model.ExamSubjects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ExamSubjectsService {

    Page<ExamSubjects> findAll(Pageable pageable);

    ExamSubjects findById(UUID id);

    List<ExamSubjects> findByExamId(UUID examId);

    ExamSubjects create(ExamSubjectRequestDto request);

    ExamSubjects update(UUID id, ExamSubjectRequestDto request);

    void delete(UUID id);
}
