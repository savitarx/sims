package com.invisos.sims.common.mapper;

import com.invisos.sims.academic.model.AcademicYears;
import com.invisos.sims.academic.model.Classes;
import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.academic.model.Subjects;
import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.common.dto.summary.AcademicYearSummaryDto;
import com.invisos.sims.common.dto.summary.ActorSummaryDto;
import com.invisos.sims.common.dto.summary.ClassSummaryDto;
import com.invisos.sims.common.dto.summary.SectionSummaryDto;
import com.invisos.sims.common.dto.summary.StudentSummaryDto;
import com.invisos.sims.common.dto.summary.SubjectSummaryDto;
import com.invisos.sims.student.model.StudentEnrollment;
import com.invisos.sims.teacher.model.Teachers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Converts shared reference entities into the compact summary DTOs that module
 * responses embed, so a client never has to resolve a bare UUID itself.
 * Module mappers pick these up via {@code @Mapper(uses = SummaryMapper.class)}.
 */
@Mapper(componentModel = "spring")
public interface SummaryMapper {

    @Mapping(source = "adminId", target = "id")
    @Mapping(source = "designation", target = "role")
    ActorSummaryDto toActor(AdminStaff adminStaff);

    @Mapping(source = "teacherId", target = "id")
    @Mapping(source = "designation", target = "role")
    ActorSummaryDto toActor(Teachers teacher);

    @Mapping(source = "classId", target = "id")
    @Mapping(source = "className", target = "name")
    ClassSummaryDto toClassSummary(Classes schoolClass);

    @Mapping(source = "subjectId", target = "id")
    @Mapping(source = "subjectName", target = "name")
    @Mapping(source = "subjectCode", target = "code")
    @Mapping(source = "subjectType", target = "type")
    SubjectSummaryDto toSubjectSummary(Subjects subject);

    @Mapping(source = "academicYearId", target = "id")
    @Mapping(source = "yearLabel", target = "label")
    AcademicYearSummaryDto toAcademicYearSummary(AcademicYears academicYear);

    @Mapping(source = "enrollmentId", target = "enrollmentId")
    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "student.name", target = "name")
    @Mapping(source = "student.admissionNumber", target = "admissionNumber")
    @Mapping(source = "rollNumber", target = "rollNumber")
    StudentSummaryDto toStudentSummary(StudentEnrollment enrollment);

    /**
     * Built by hand so {@code displayName} can combine the class and section
     * names ("Grade 10 - A") without tripping over a null class.
     */
    default SectionSummaryDto toSectionSummary(Sections section) {
        if (section == null) {
            return null;
        }
        Classes schoolClass = section.getSchoolClass();
        String className = schoolClass == null ? null : schoolClass.getClassName();
        String displayName = className == null
                ? section.getSectionName()
                : className + " - " + section.getSectionName();

        return SectionSummaryDto.builder()
                .id(section.getSectionId())
                .name(section.getSectionName())
                .classId(schoolClass == null ? null : schoolClass.getClassId())
                .className(className)
                .displayName(displayName)
                .build();
    }
}
