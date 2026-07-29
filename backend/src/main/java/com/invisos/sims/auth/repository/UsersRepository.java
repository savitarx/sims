package com.invisos.sims.auth.repository;

import com.invisos.sims.auth.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {

    boolean existsByLoginId(String loginId);
}
