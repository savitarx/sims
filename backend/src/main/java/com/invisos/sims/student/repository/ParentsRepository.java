package com.invisos.sims.student.repository;

import com.invisos.sims.student.model.Parents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParentsRepository extends JpaRepository<Parents, UUID> {
}
