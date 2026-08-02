package com.invisos.sims.auth.repository;

import com.invisos.sims.auth.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {

    boolean existsByLoginId(String loginId);

    List<Users> findByEmailIn(Collection<String> emails);
}
