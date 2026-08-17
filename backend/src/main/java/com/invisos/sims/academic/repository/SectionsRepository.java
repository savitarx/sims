package com.invisos.sims.academic.repository;

import com.invisos.sims.academic.model.AcademicYears;
import com.invisos.sims.academic.model.Classes;
import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.teacher.model.Teachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SectionsRepository extends JpaRepository<Sections, UUID> {
    boolean existsByClassTeacherAndAcademicYear(Teachers classTeacher, AcademicYears academicYear);

    boolean existsBySchoolClassAndAcademicYearAndSectionName(Classes schoolClass, AcademicYears academicYear, String sectionName);

    List<Sections> findAllByAcademicYear_AcademicYearIdAndSchoolClass_ClassIdOrderBySectionNameAsc(UUID academicYearId, UUID classId);

    List<Sections> findAllByAcademicYear_AcademicYearIdOrderBySchoolClass_ClassNameAscSectionNameAsc(
            UUID academicYearId
    );
}
