package com.invisos.sims.attendance.service;

import com.invisos.sims.attendance.model.ClassTimetable;

import java.util.List;
import java.util.UUID;

public interface ClassTimetableService {

    List<ClassTimetable> findAll();

    ClassTimetable findById(UUID id);

    ClassTimetable create(ClassTimetable entity);

    ClassTimetable update(UUID id, ClassTimetable entity);

    void delete(UUID id);
}
