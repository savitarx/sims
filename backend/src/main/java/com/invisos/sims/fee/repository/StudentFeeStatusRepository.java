package com.invisos.sims.fee.repository;

import com.invisos.sims.common.enums.FeeStatus;
import com.invisos.sims.fee.model.StudentFeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentFeeStatusRepository extends JpaRepository<StudentFeeStatus, UUID> {

    @Override
    @EntityGraph(attributePaths = {
            "enrollment", "enrollment.student", "enrollment.section", "enrollment.section.schoolClass",
            "fee", "fee.schoolClass", "fee.academicYear", "updatedBy", "updatedByAdmin"})
    Page<StudentFeeStatus> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {
            "enrollment", "enrollment.student", "enrollment.section", "enrollment.section.schoolClass",
            "fee", "fee.schoolClass", "fee.academicYear", "updatedBy", "updatedByAdmin"})
    Optional<StudentFeeStatus> findByStudentFeeStatusId(UUID studentFeeStatusId);

    @EntityGraph(attributePaths = {
            "enrollment", "enrollment.student", "enrollment.section", "enrollment.section.schoolClass",
            "fee", "fee.schoolClass", "fee.academicYear", "updatedBy", "updatedByAdmin"})
    List<StudentFeeStatus> findByEnrollmentEnrollmentId(UUID enrollmentId);

    /** Fee status sheet: every status recorded for one fee within one section. */
    @EntityGraph(attributePaths = {
            "enrollment", "enrollment.student", "updatedBy", "updatedByAdmin"})
    List<StudentFeeStatus> findByFeeFeeIdAndEnrollmentSectionSectionId(UUID feeId, UUID sectionId);

    /** Defaulters: statuses in the given states, optionally narrowed by class/year. */
    @EntityGraph(attributePaths = {
            "enrollment", "enrollment.student", "enrollment.section", "enrollment.section.schoolClass",
            "fee", "fee.schoolClass", "fee.academicYear", "updatedBy", "updatedByAdmin"})
    Page<StudentFeeStatus> findByStatusIn(Collection<FeeStatus> statuses, Pageable pageable);

    @EntityGraph(attributePaths = {
            "enrollment", "enrollment.student", "enrollment.section", "enrollment.section.schoolClass",
            "fee", "fee.schoolClass", "fee.academicYear", "updatedBy", "updatedByAdmin"})
    Page<StudentFeeStatus> findByStatusInAndFeeSchoolClassClassId(Collection<FeeStatus> statuses,
                                                                 UUID classId,
                                                                 Pageable pageable);

    @EntityGraph(attributePaths = {
            "enrollment", "enrollment.student", "enrollment.section", "enrollment.section.schoolClass",
            "fee", "fee.schoolClass", "fee.academicYear", "updatedBy", "updatedByAdmin"})
    Page<StudentFeeStatus> findByStatusInAndFeeAcademicYearAcademicYearId(Collection<FeeStatus> statuses,
                                                                         UUID academicYearId,
                                                                         Pageable pageable);

    @EntityGraph(attributePaths = {
            "enrollment", "enrollment.student", "enrollment.section", "enrollment.section.schoolClass",
            "fee", "fee.schoolClass", "fee.academicYear", "updatedBy", "updatedByAdmin"})
    Page<StudentFeeStatus> findByStatusInAndFeeSchoolClassClassIdAndFeeAcademicYearAcademicYearId(
            Collection<FeeStatus> statuses, UUID classId, UUID academicYearId, Pageable pageable);

    boolean existsByEnrollmentEnrollmentIdAndFeeFeeId(UUID enrollmentId, UUID feeId);

    boolean existsByFeeFeeId(UUID feeId);

    long countByFeeFeeIdAndStatus(UUID feeId, FeeStatus status);
}
