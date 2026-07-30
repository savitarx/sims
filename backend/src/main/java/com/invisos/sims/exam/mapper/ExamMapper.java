package com.invisos.sims.exam.mapper;

import com.invisos.sims.exam.dto.request.ExamRequestDto;
import com.invisos.sims.exam.dto.response.ExamResponseDto;
import com.invisos.sims.exam.model.Exams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamMapper {

    @Mapping(target = "examId", ignore = true)
    @Mapping(target = "academicYear", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Exams toEntity(ExamRequestDto dto);

    @Mapping(target = "academicYear", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ExamRequestDto dto, @MappingTarget Exams entity);

    @Mapping(source = "academicYear.academicYearId", target = "academicYearId")
    @Mapping(source = "createdBy.adminId", target = "createdById")
    ExamResponseDto toResponse(Exams entity);

    List<ExamResponseDto> toResponseList(List<Exams> entities);
}