package com.invisos.sims.attendance.mapper;

import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.academic.model.Subjects;
import com.invisos.sims.attendance.dto.ClassTimeTableResponseDto;
import com.invisos.sims.attendance.dto.ClassTimeTableRequestDto;
import com.invisos.sims.attendance.model.ClassTimetable;
import com.invisos.sims.teacher.model.Teachers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassTimeTableMapper {

    @Mapping(target = "sectionId",source = "section.sectionId")
    @Mapping(target = "sectionName",source = "section.sectionName")
    @Mapping(target = "subjectId",source = "subject.subjectId")
    @Mapping(target = "subjectName",source="subject.subjectName")
    @Mapping(target = "teacherId", source = "teacher.teacherId")
    @Mapping(target = "teacherName", source = "teacher.name")
    @Mapping(target = "teacherStatus",source = "teacher.status")
    ClassTimeTableResponseDto toResponse(ClassTimetable timetable);

    @Mapping(target = "timetableId", ignore = true)
    @Mapping(target = "section", source = "section")
    @Mapping(target = "subject", source = "subject")
    @Mapping(target = "teacher", source = "teacher")
    ClassTimetable toEntity(
            ClassTimeTableRequestDto dto,
            Sections section,
            Subjects subject,
            Teachers teacher
    );

    List<ClassTimeTableResponseDto> toResponseList(List<ClassTimetable> classTimetables);
}
