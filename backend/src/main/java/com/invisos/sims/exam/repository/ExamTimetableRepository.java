package com.invisos.sims.exam.repository;

import com.invisos.sims.exam.model.ExamTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExamTimetableRepository extends JpaRepository<ExamTimetable, UUID> {
}
