package com.invisos.sims.exam.mapper;

import com.invisos.sims.exam.dto.request.ExamTimetableRequestDto;
import com.invisos.sims.exam.dto.response.ExamTimetableResponseDto;
import com.invisos.sims.exam.model.ExamTimetable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamTimetableMapper {

    @Mapping(target = "examTimetableId", ignore = true)
    @Mapping(target = "examSubject", ignore = true)
    ExamTimetable toEntity(ExamTimetableRequestDto dto);

    @Mapping(target = "examSubject", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ExamTimetableRequestDto dto, @MappingTarget ExamTimetable entity);

    @Mapping(source = "examSubject.examSubjectId", target = "examSubjectId")
    ExamTimetableResponseDto toResponse(ExamTimetable entity);

    List<ExamTimetableResponseDto> toResponseList(List<ExamTimetable> entities);
}
