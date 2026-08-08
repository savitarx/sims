package com.invisos.sims.teacher.mapper;

import com.invisos.sims.academic.model.AcademicYears;
import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.academic.model.Subjects;
import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.teacher.dto.TeachersAssignmentResponseDto;
import com.invisos.sims.teacher.model.TeacherAssignment;
import com.invisos.sims.teacher.model.Teachers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeachersAssignmentMapper {

    @Mapping(target = "assignmentId", ignore = true)
    @Mapping(target = "teacher", source = "teacher")
    @Mapping(target = "academicYear", source = "academicYear")
    @Mapping(target = "section", source = "section")
    @Mapping(target = "subject", source = "subject")
    @Mapping(target = "assignedBy", source = "assignedBy")
    TeacherAssignment toEntity(Teachers teacher,
                               AcademicYears academicYear,
                               Sections section,
                               Subjects subject,
                               AdminStaff assignedBy);

    @Mapping(target = "teacherId", source = "teacher.teacherId")
    @Mapping(target = "teacherName", source = "teacher.name")
    @Mapping(target = "academicYearId", source = "academicYear.academicYearId")
    @Mapping(target = "academicYearLabel", source = "academicYear.yearLabel")
    @Mapping(target = "sectionId", source = "section.sectionId")
    @Mapping(target = "sectionName", source = "section.sectionName")
    @Mapping(target = "subjectId", source = "subject.subjectId")
    @Mapping(target = "subjectName", source = "subject.subjectName")
    @Mapping(target = "assignedById", source = "assignedBy.adminId")
    @Mapping(target = "assignedByName", source = "assignedBy.name")
    TeachersAssignmentResponseDto toResponseDto(TeacherAssignment entity);

    List<TeachersAssignmentResponseDto> toResponseDtoList(List<TeacherAssignment> entities);
}