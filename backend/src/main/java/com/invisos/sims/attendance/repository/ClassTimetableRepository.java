package com.invisos.sims.attendance.repository;

import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.attendance.model.ClassTimetable;
import com.invisos.sims.common.enums.DayOfWeek;
import com.invisos.sims.teacher.model.Teachers;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClassTimetableRepository extends JpaRepository<ClassTimetable, UUID> {
    boolean existsBySectionAndDayAndPeriodNumber(Sections section, @NotNull(message = "Day is required.") DayOfWeek day, @NotNull(message = "Period number is required.") @Positive(message = "Period number must be greater than 0.") Integer periodNumber);

    @Query("""
    SELECT c
    FROM ClassTimetable c
    WHERE c.section.sectionId = :sectionId
      AND c.section.academicYear.academicYearId = :academicYearId
""")
    List<ClassTimetable> findBySectionAndAcademicYear(
            @Param("sectionId") UUID sectionId,
            @Param("academicYearId") UUID academicYearId
    );

    @Query("""
    SELECT c
    FROM ClassTimetable c
    WHERE c.section.schoolClass.classId = :classId
      AND c.section.academicYear.academicYearId = :academicYearId
""")
    List<ClassTimetable> findByClassAndAcademicYear(UUID classId, UUID academicYearId);


    @Query("""
    SELECT c
    FROM ClassTimetable c
    WHERE c.section.academicYear.academicYearId = :academicYearId
""")
    List<ClassTimetable> findByAcademicYear(UUID academicYearId);

    boolean existsByTeacherAndDayAndPeriodNumber(Teachers teacher, DayOfWeek day,  Integer periodNumber);

    boolean existsBySectionAndDayAndPeriodNumberAndTimetableIdNot(Sections section,  DayOfWeek day,  Integer periodNumber, UUID id);

    boolean existsByTeacherAndDayAndPeriodNumberAndTimetableIdNot(Teachers teacher, DayOfWeek day,Integer periodNumber, UUID id);
}
