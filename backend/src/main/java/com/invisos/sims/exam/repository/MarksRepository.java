package com.invisos.sims.exam.repository;

import com.invisos.sims.exam.model.Marks;
import com.invisos.sims.exam.repository.projection.ExamSubjectMarkCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MarksRepository extends JpaRepository<Marks, UUID> {

    @Override
    @EntityGraph(attributePaths = {
            "enrollment", "enrollment.student", "enrollment.section",
            "examSubject", "examSubject.subject", "examSubject.exam", "enteredBy", "updatedBy"})
    Page<Marks> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {
            "enrollment", "enrollment.student", "enrollment.section",
            "examSubject", "examSubject.subject", "examSubject.exam", "enteredBy", "updatedBy"})
    Optional<Marks> findByMarkId(UUID markId);

    @EntityGraph(attributePaths = {
            "enrollment", "enrollment.student", "enrollment.section",
            "examSubject", "examSubject.subject", "examSubject.exam", "enteredBy", "updatedBy"})
    Page<Marks> findByExamSubjectExamSubjectId(UUID examSubjectId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "enrollment", "enrollment.student", "enrollment.section",
            "examSubject", "examSubject.subject", "examSubject.exam", "enteredBy", "updatedBy"})
    List<Marks> findByEnrollmentEnrollmentId(UUID enrollmentId);

    /** Marks sheet: every mark already recorded for one subject in one section. */
    @EntityGraph(attributePaths = {"enrollment", "enrollment.student", "enteredBy", "updatedBy"})
    List<Marks> findByExamSubjectExamSubjectIdAndEnrollmentSectionSectionId(UUID examSubjectId,
                                                                           UUID sectionId);

    /** Result summary: every mark a student scored across one exam. */
    @EntityGraph(attributePaths = {"examSubject", "examSubject.subject", "enrollment", "enrollment.student"})
    List<Marks> findByExamSubjectExamExamIdAndEnrollmentEnrollmentId(UUID examId, UUID enrollmentId);

    /** Result summary for a whole section. */
    @EntityGraph(attributePaths = {"examSubject", "examSubject.subject", "enrollment", "enrollment.student"})
    List<Marks> findByExamSubjectExamExamIdAndEnrollmentSectionSectionId(UUID examId, UUID sectionId);

    boolean existsByEnrollmentEnrollmentIdAndExamSubjectExamSubjectId(UUID enrollmentId,
                                                                     UUID examSubjectId);

    /** Guards deletion of exam subjects and exams. */
    boolean existsByExamSubjectExamSubjectId(UUID examSubjectId);

    boolean existsByExamSubjectExamExamId(UUID examId);

    long countByExamSubjectExamSubjectId(UUID examSubjectId);

    /** Highest mark recorded for a subject; guards lowering its maximum. */
    @Query("SELECT MAX(m.marksObtained) FROM Marks m WHERE m.examSubject.examSubjectId = :examSubjectId")
    BigDecimal findHighestMarkObtained(@Param("examSubjectId") UUID examSubjectId);

    /**
     * Marks entered per subject for one exam, in a single grouped query — used by
     * the exam overview so progress does not cost one query per subject.
     */
    @Query("""
            SELECT m.examSubject.examSubjectId AS examSubjectId, COUNT(m) AS total
            FROM Marks m
            WHERE m.examSubject.exam.examId = :examId
            GROUP BY m.examSubject.examSubjectId
            """)
    List<ExamSubjectMarkCount> countMarksPerExamSubject(@Param("examId") UUID examId);
}
