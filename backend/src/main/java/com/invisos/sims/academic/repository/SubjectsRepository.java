package com.invisos.sims.academic.repository;

import com.invisos.sims.academic.model.Subjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubjectsRepository extends JpaRepository<Subjects, UUID> {

    Optional<Subjects> findBySubjectId(UUID subjectId);

    boolean existsBySubjectCode(String subjectCode);
}
