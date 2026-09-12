package com.invisos.sims.student.repository;

import com.invisos.sims.common.enums.RecordStatus;
import com.invisos.sims.student.model.StudentEnrollment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, UUID> {

    @EntityGraph(attributePaths = {"student", "section", "section.schoolClass", "academicYear"})
    Optional<StudentEnrollment> findByEnrollmentId(UUID enrollmentId);

    /**
     * Section roster, used by the exam marks sheet and the fee status sheet.
     * Ordered by roll number so the grid matches the printed register.
     */
    @EntityGraph(attributePaths = {"student", "section", "section.schoolClass"})
    List<StudentEnrollment> findBySectionSectionIdAndStatusOrderByRollNumberAsc(UUID sectionId,
                                                                               RecordStatus status);

    @EntityGraph(attributePaths = {"student", "section", "section.schoolClass"})
    List<StudentEnrollment> findBySectionSectionIdOrderByRollNumberAsc(UUID sectionId);

    long countBySectionSectionIdAndStatus(UUID sectionId, RecordStatus status);
}
