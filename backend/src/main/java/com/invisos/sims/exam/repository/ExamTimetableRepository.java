package com.invisos.sims.exam.repository;

import com.invisos.sims.exam.model.ExamTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExamTimetableRepository extends JpaRepository<ExamTimetable, UUID> {

    Optional<ExamTimetable> findByExamTimetableId(UUID examTimetableId);

    List<ExamTimetable> findByExamSubjectExamExamId(UUID examId);

    boolean existsByExamSubjectExamSubjectId(UUID examSubjectId);
}
