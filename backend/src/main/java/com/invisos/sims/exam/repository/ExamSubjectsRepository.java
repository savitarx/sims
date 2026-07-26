package com.invisos.sims.exam.repository;

import com.invisos.sims.exam.model.ExamSubjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExamSubjectsRepository extends JpaRepository<ExamSubjects, UUID> {

    boolean existsByExamExamIdAndSubjectSubjectIdAndSchoolClassClassId(
            UUID examId, UUID subjectId, UUID classId
    );
}
