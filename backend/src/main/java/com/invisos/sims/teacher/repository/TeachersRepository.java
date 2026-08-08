package com.invisos.sims.teacher.repository;

import com.invisos.sims.auth.model.Users;
import com.invisos.sims.common.enums.UserStatus;
import com.invisos.sims.teacher.model.Teachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TeachersRepository extends JpaRepository<Teachers, UUID> {
    boolean existsByEmployeeId(String employeeId);

    List<Teachers> findByStatus(UserStatus userStatus);

    boolean existsByEmployeeIdAndTeacherIdNot(String employeeId, UUID id);


    @Query("""
            SELECT t
            FROM Teachers t
            WHERE t.status = com.invisos.sims.common.enums.UserStatus.ACTIVE
            AND (
                LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(t.parentName) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(t.employeeId) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(t.user.email) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            """)
    List<Teachers> searchTeachers(@Param("query") String query);

    long count();

    long countByStatus(UserStatus status);

    List<Teachers> findByEmployeeIdIn(Set<String> employeeIds);



    @Query("""
    SELECT DISTINCT t
    FROM Teachers t
    JOIN TeacherAssignment ta ON ta.teacher = t
    WHERE ta.subject.subjectId = :subjectId
      AND t.status = :status
""")
    List<Teachers> findBySubjectIdAndStatus(
            UUID subjectId,
            UserStatus status
    );
}

