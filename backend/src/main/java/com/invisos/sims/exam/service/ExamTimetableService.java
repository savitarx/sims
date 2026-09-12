package com.invisos.sims.exam.service;

import com.invisos.sims.exam.dto.request.ExamTimetableRequestDto;
import com.invisos.sims.exam.model.ExamTimetable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ExamTimetableService {

    Page<ExamTimetable> findAll(Pageable pageable);

    ExamTimetable findById(UUID id);

    /** Full schedule of one exam, ordered by date and time. */
    List<ExamTimetable> findByExamId(UUID examId);

    ExamTimetable create(ExamTimetableRequestDto request);

    ExamTimetable update(UUID id, ExamTimetableRequestDto request);

    void delete(UUID id);
}
