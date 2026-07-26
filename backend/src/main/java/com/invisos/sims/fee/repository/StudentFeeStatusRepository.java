package com.invisos.sims.fee.repository;

import com.invisos.sims.fee.model.StudentFeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentFeeStatusRepository extends JpaRepository<StudentFeeStatus, UUID> {
}
