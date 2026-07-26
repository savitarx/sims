package com.invisos.sims.exam.service;

import com.invisos.sims.exam.model.ExamSubjects;

import java.util.List;
import java.util.UUID;

public interface ExamSubjectsService {

    List<ExamSubjects> findAll();

    ExamSubjects findById(UUID id);

    ExamSubjects create(ExamSubjects entity);

    ExamSubjects update(UUID id, ExamSubjects entity);

    void delete(UUID id);
}
