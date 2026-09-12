package com.invisos.sims.academic.repository;

import com.invisos.sims.academic.model.Sections;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SectionsRepository extends JpaRepository<Sections, UUID> {

    @EntityGraph(attributePaths = {"schoolClass", "academicYear", "classTeacher"})
    Optional<Sections> findBySectionId(UUID sectionId);

    @EntityGraph(attributePaths = {"schoolClass", "academicYear"})
    List<Sections> findBySchoolClassClassId(UUID classId);
}
