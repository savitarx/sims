package com.invisos.sims.academic.repository;

import com.invisos.sims.academic.model.AcademicYears;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AcademicYearsRepository extends JpaRepository<AcademicYears, UUID> {

    Optional<AcademicYears> findByAcademicYearId(UUID academicYearId);
}
