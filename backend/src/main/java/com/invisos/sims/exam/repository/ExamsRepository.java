package com.invisos.sims.exam.repository;

import com.invisos.sims.exam.model.Exams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExamsRepository extends JpaRepository<Exams, UUID> {

    Optional<Exams> findByExamId(UUID examId);

    boolean existsByExamNameAndAcademicYearAcademicYearId(
            String examName, UUID academicYearId
    );
}
