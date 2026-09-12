package com.invisos.sims.fee.repository;

import com.invisos.sims.fee.model.Fees;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeesRepository extends JpaRepository<Fees, UUID> {

    @Override
    @EntityGraph(attributePaths = {"schoolClass", "academicYear", "updatedBy"})
    Page<Fees> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"schoolClass", "academicYear", "updatedBy"})
    Optional<Fees> findByFeeId(UUID feeId);

    @EntityGraph(attributePaths = {"schoolClass", "academicYear", "updatedBy"})
    Page<Fees> findBySchoolClassClassId(UUID classId, Pageable pageable);

    @EntityGraph(attributePaths = {"schoolClass", "academicYear", "updatedBy"})
    Page<Fees> findByAcademicYearAcademicYearId(UUID academicYearId, Pageable pageable);

    @EntityGraph(attributePaths = {"schoolClass", "academicYear", "updatedBy"})
    Page<Fees> findBySchoolClassClassIdAndAcademicYearAcademicYearId(UUID classId,
                                                                    UUID academicYearId,
                                                                    Pageable pageable);

    boolean existsBySchoolClassClassIdAndAcademicYearAcademicYearIdAndTermName(
            UUID classId, UUID yearId, String term
    );

    /** Duplicate check for updates: same definition, but a different fee. */
    boolean existsBySchoolClassClassIdAndAcademicYearAcademicYearIdAndTermNameAndFeeIdNot(
            UUID classId, UUID yearId, String term, UUID feeId
    );
}
