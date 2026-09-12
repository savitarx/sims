package com.invisos.sims.exam.mapper;

import com.invisos.sims.common.mapper.SummaryMapper;
import com.invisos.sims.exam.dto.request.ExamTimetableRequestDto;
import com.invisos.sims.exam.dto.response.ExamTimetableResponseDto;
import com.invisos.sims.exam.model.ExamTimetable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SummaryMapper.class, ExamMapper.class})
public interface ExamTimetableMapper {

    @Mapping(target = "examTimetableId", ignore = true)
    @Mapping(target = "examSubject", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ExamTimetable toEntity(ExamTimetableRequestDto dto);

    @Mapping(target = "examSubject", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ExamTimetableRequestDto dto, @MappingTarget ExamTimetable entity);

    @Mapping(source = "examSubject.exam", target = "exam")
    @Mapping(source = "examSubject.subject", target = "subject")
    @Mapping(source = "examSubject.schoolClass", target = "schoolClass")
    @Mapping(source = "examSubject.maxMarks", target = "maxMarks")
    @Mapping(source = "examSubject.examSubjectId", target = "examSubjectId")
    ExamTimetableResponseDto toResponse(ExamTimetable entity);

    List<ExamTimetableResponseDto> toResponseList(List<ExamTimetable> entities);
}
