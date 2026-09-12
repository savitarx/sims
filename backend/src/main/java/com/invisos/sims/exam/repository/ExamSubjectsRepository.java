package com.invisos.sims.exam.repository;

import com.invisos.sims.exam.model.ExamSubjects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExamSubjectsRepository extends JpaRepository<ExamSubjects, UUID> {

    @Override
    @EntityGraph(attributePaths = {"exam", "subject", "schoolClass", "updatedBy"})
    Page<ExamSubjects> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"exam", "subject", "schoolClass", "updatedBy"})
    Optional<ExamSubjects> findByExamSubjectId(UUID examSubjectId);

    @EntityGraph(attributePaths = {"exam", "subject", "schoolClass", "updatedBy"})
    List<ExamSubjects> findByExamExamId(UUID examId);

    @EntityGraph(attributePaths = {"exam", "subject", "schoolClass", "updatedBy"})
    List<ExamSubjects> findByExamExamIdAndSchoolClassClassId(UUID examId, UUID classId);

    boolean existsByExamExamIdAndSubjectSubjectIdAndSchoolClassClassId(
            UUID examId, UUID subjectId, UUID classId
    );

    /** Used by the publish readiness check. */
    long countByExamExamId(UUID examId);
}
