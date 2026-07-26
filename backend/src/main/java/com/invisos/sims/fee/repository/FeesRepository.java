package com.invisos.sims.fee.repository;

import com.invisos.sims.fee.model.Fees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeesRepository extends JpaRepository<Fees, UUID> {

    boolean existsBySchoolClassClassIdAndAcademicYearAcademicYearIdAndTermName(
            UUID classId, UUID yearId, String term
    );
}
