package com.invisos.sims.student.repository;

import com.invisos.sims.student.model.StudentSubjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentSubjectsRepository extends JpaRepository<StudentSubjects, UUID> {
}
