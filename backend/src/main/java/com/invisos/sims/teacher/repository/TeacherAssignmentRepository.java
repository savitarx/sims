package com.invisos.sims.teacher.repository;

import com.invisos.sims.academic.model.AcademicYears;
import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.academic.model.Subjects;
import com.invisos.sims.common.enums.UserStatus;
import com.invisos.sims.teacher.model.TeacherAssignment;
import com.invisos.sims.teacher.model.Teachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeacherAssignmentRepository extends JpaRepository<TeacherAssignment, UUID> {
    boolean existsByTeacherAndAcademicYearAndSectionAndSubject(Teachers teacher, AcademicYears academicYear, Sections section, Subjects subject);

    List<TeacherAssignment> findByTeacher_TeacherId(UUID teacherId);

    @Query("SELECT t FROM TeacherAssignment t WHERE t.teacher.status = :status")
    List<TeacherAssignment> findByTeacherStatus(@Param("status") UserStatus status);

    @Query("select t from TeacherAssignment t where t.section.sectionId=:sectionId and t.teacher.status=:userStatus")
    List<TeacherAssignment> findBySection_SectionIdAndTeacher_Status(
            UUID sectionId,
            UserStatus status
    );
}
