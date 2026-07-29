package com.invisos.sims.exam.service;

import com.invisos.sims.exam.dto.request.ExamTimetableRequestDto;
import com.invisos.sims.exam.model.ExamTimetable;

import java.util.List;
import java.util.UUID;

public interface ExamTimetableService {

    List<ExamTimetable> findAll();

    ExamTimetable findById(UUID id);

    List<ExamTimetable> findByExamId(UUID examId);

    ExamTimetable create(ExamTimetableRequestDto request);

    ExamTimetable update(UUID id, ExamTimetableRequestDto request);

    void delete(UUID id);
}
