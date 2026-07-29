package com.invisos.sims.teacher.repository;

import com.invisos.sims.teacher.model.Teachers;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeachersRepository extends JpaRepository<Teachers, UUID> {
    boolean existsByEmployeeId(String employeeId);
}
