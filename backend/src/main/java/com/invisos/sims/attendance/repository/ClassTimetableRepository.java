package com.invisos.sims.attendance.repository;

import com.invisos.sims.attendance.model.ClassTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClassTimetableRepository extends JpaRepository<ClassTimetable, UUID> {
}
