package com.invisos.sims.exam.service;

import com.invisos.sims.exam.model.ExamTimetable;

import java.util.List;
import java.util.UUID;

public interface ExamTimetableService {

    List<ExamTimetable> findAll();

    ExamTimetable findById(UUID id);

    ExamTimetable create(ExamTimetable entity);

    ExamTimetable update(UUID id, ExamTimetable entity);

    void delete(UUID id);
}
