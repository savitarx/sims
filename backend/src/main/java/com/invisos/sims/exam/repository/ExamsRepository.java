package com.invisos.sims.exam.repository;

import com.invisos.sims.common.enums.ExamStatus;
import com.invisos.sims.exam.model.Exams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExamsRepository extends JpaRepository<Exams, UUID> {

    // Associations are fetched with the row because every response embeds them.

    @Override
    @EntityGraph(attributePaths = {"academicYear", "createdBy", "updatedBy"})
    Page<Exams> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"academicYear", "createdBy", "updatedBy"})
    Optional<Exams> findByExamId(UUID examId);

    @EntityGraph(attributePaths = {"academicYear", "createdBy", "updatedBy"})
    Page<Exams> findByAcademicYearAcademicYearId(UUID academicYearId, Pageable pageable);

    @EntityGraph(attributePaths = {"academicYear", "createdBy", "updatedBy"})
    Page<Exams> findByStatus(ExamStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"academicYear", "createdBy", "updatedBy"})
    Page<Exams> findByAcademicYearAcademicYearIdAndStatus(UUID academicYearId,
                                                         ExamStatus status,
                                                         Pageable pageable);

    boolean existsByExamNameAndAcademicYearAcademicYearId(String examName, UUID academicYearId);

    /** Duplicate check for updates: same name in the year, but a different exam. */
    boolean existsByExamNameAndAcademicYearAcademicYearIdAndExamIdNot(String examName,
                                                                     UUID academicYearId,
                                                                     UUID examId);
}
