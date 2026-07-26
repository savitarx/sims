package com.invisos.sims.attendance.service;

import com.invisos.sims.attendance.model.Attendance;

import java.util.List;
import java.util.UUID;

public interface AttendanceService {

    List<Attendance> findAll();

    Attendance findById(UUID id);

    Attendance create(Attendance entity);

    Attendance update(UUID id, Attendance entity);

    void delete(UUID id);
}
