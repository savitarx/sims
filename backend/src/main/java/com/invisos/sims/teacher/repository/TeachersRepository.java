package com.invisos.sims.teacher.repository;

import com.invisos.sims.common.enums.UserStatus;
import com.invisos.sims.teacher.model.Teachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeachersRepository extends JpaRepository<Teachers, UUID> {
    boolean existsByEmployeeId(String employeeId);

    List<Teachers> findByStatus(UserStatus userStatus);

    boolean existsByEmployeeIdAndTeacherIdNot(String employeeId, UUID id);
}
