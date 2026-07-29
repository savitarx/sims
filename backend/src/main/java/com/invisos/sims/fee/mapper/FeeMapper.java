package com.invisos.sims.fee.mapper;

import com.invisos.sims.fee.dto.request.FeeRequestDto;
import com.invisos.sims.fee.dto.response.FeeResponseDto;
import com.invisos.sims.fee.model.Fees;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeeMapper {

    @Mapping(target = "feeId", ignore = true)
    @Mapping(target = "schoolClass", ignore = true)
    @Mapping(target = "academicYear", ignore = true)
    Fees toEntity(FeeRequestDto dto);

    @Mapping(target = "schoolClass", ignore = true)
    @Mapping(target = "academicYear", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(FeeRequestDto dto, @MappingTarget Fees entity);

    @Mapping(source = "schoolClass.classId", target = "classId")
    @Mapping(source = "academicYear.academicYearId", target = "academicYearId")
    FeeResponseDto toResponse(Fees entity);

    List<FeeResponseDto> toResponseList(List<Fees> entities);
}
