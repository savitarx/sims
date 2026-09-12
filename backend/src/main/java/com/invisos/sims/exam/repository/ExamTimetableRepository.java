package com.invisos.sims.exam.repository;

import com.invisos.sims.exam.model.ExamTimetable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExamTimetableRepository extends JpaRepository<ExamTimetable, UUID> {

    @Override
    @EntityGraph(attributePaths = {
            "examSubject", "examSubject.exam", "examSubject.subject", "examSubject.schoolClass", "updatedBy"})
    Page<ExamTimetable> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {
            "examSubject", "examSubject.exam", "examSubject.subject", "examSubject.schoolClass", "updatedBy"})
    Optional<ExamTimetable> findByExamTimetableId(UUID examTimetableId);

    @EntityGraph(attributePaths = {
            "examSubject", "examSubject.exam", "examSubject.subject", "examSubject.schoolClass", "updatedBy"})
    List<ExamTimetable> findByExamSubjectExamExamIdOrderByExamDateAscExamTimeAsc(UUID examId);

    boolean existsByExamSubjectExamSubjectId(UUID examSubjectId);

    /** Used by the publish readiness check. */
    long countByExamSubjectExamExamId(UUID examId);
}
