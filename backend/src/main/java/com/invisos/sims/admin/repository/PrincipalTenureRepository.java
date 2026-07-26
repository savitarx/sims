package com.invisos.sims.admin.repository;

import com.invisos.sims.admin.model.PrincipalTenure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrincipalTenureRepository extends JpaRepository<PrincipalTenure, UUID> {
}
