package com.invisos.sims.academic.mapper;

import com.invisos.sims.academic.dto.request.SectionRequestDto;
import com.invisos.sims.academic.dto.response.SectionResponseDto;
import com.invisos.sims.academic.model.Sections;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SectionMapper {
    @Mapping(target = "classId", source = "schoolClass.classId")
    @Mapping(target = "className", source = "schoolClass.className")

    @Mapping(target = "academicYearId", source = "academicYear.academicYearId")
    @Mapping(target = "academicYearName", source = "academicYear.yearLabel")

    @Mapping(target = "classTeacherId", source = "classTeacher.teacherId")
    @Mapping(target = "classTeacherName", source = "classTeacher.name")
    @Mapping(target = "classTeacherStatus", source = "classTeacher.status")

//    @Mapping(target = "assignedById", source = "assignedBy.adminId")
    SectionResponseDto toResponse(Sections section);

    @Mapping(target = "sectionId", ignore = true)
    @Mapping(target = "schoolClass", ignore = true)
    @Mapping(target = "academicYear", ignore = true)
    @Mapping(target = "classTeacher", ignore = true)
    @Mapping(target = "assignedBy", ignore = true)
    Sections toEntity(SectionRequestDto requestDto);

    List<SectionResponseDto> toResponseList(List<Sections> sections);
}
