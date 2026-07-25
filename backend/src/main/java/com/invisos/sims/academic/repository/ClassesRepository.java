package com.invisos.sims.academic.repository;

import com.invisos.sims.academic.model.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, UUID> {
}
