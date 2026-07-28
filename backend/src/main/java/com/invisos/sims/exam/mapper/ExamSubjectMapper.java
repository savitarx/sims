package com.invisos.sims.exam.mapper;

import com.invisos.sims.exam.dto.request.ExamSubjectRequestDto;
import com.invisos.sims.exam.dto.response.ExamSubjectResponseDto;
import com.invisos.sims.exam.model.ExamSubjects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamSubjectMapper {

    @Mapping(target = "examSubjectId", ignore = true)
    @Mapping(target = "exam", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "schoolClass", ignore = true)
    ExamSubjects toEntity(ExamSubjectRequestDto dto);

    @Mapping(target = "exam", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "schoolClass", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ExamSubjectRequestDto dto, @MappingTarget ExamSubjects entity);

    @Mapping(source = "exam.examId", target = "examId")
    @Mapping(source = "subject.subjectId", target = "subjectId")
    @Mapping(source = "schoolClass.classId", target = "classId")
    ExamSubjectResponseDto toResponse(ExamSubjects entity);

    List<ExamSubjectResponseDto> toResponseList(List<ExamSubjects> entities);
}
