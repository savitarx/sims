package com.invisos.sims.exam.repository;

import com.invisos.sims.exam.model.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MarksRepository extends JpaRepository<Marks, UUID> {

    Optional<Marks> findByMarkId(UUID markId);

    List<Marks> findByExamSubjectExamSubjectId(UUID examSubjectId);

    List<Marks> findByEnrollmentEnrollmentId(UUID enrollmentId);

    boolean existsByEnrollmentEnrollmentIdAndExamSubjectExamSubjectId(
            UUID enrollmentId, UUID examSubjectId
    );
}
