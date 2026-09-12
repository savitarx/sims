package com.invisos.sims.exam.mapper;

import com.invisos.sims.common.mapper.SummaryMapper;
import com.invisos.sims.exam.dto.request.ExamRequestDto;
import com.invisos.sims.exam.dto.response.ExamResponseDto;
import com.invisos.sims.exam.dto.response.ExamSummaryDto;
import com.invisos.sims.exam.model.Exams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = SummaryMapper.class)
public interface ExamMapper {

    @Mapping(target = "examId", ignore = true)
    @Mapping(target = "academicYear", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Exams toEntity(ExamRequestDto dto);

    @Mapping(target = "academicYear", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ExamRequestDto dto, @MappingTarget Exams entity);

    @Mapping(source = "academicYear.academicYearId", target = "academicYearId")
    @Mapping(source = "createdBy.adminId", target = "createdById")
    ExamResponseDto toResponse(Exams entity);

    List<ExamResponseDto> toResponseList(List<Exams> entities);

    @Mapping(source = "examId", target = "id")
    @Mapping(source = "examName", target = "name")
    ExamSummaryDto toSummary(Exams entity);
}
