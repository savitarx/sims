package com.invisos.sims.fee.mapper;

import com.invisos.sims.fee.dto.request.StudentFeeStatusRequestDto;
import com.invisos.sims.fee.dto.response.StudentFeeStatusResponseDto;
import com.invisos.sims.fee.model.StudentFeeStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentFeeStatusMapper {

    @Mapping(target = "studentFeeStatusId", ignore = true)
    @Mapping(target = "enrollment", ignore = true)
    @Mapping(target = "fee", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    StudentFeeStatus toEntity(StudentFeeStatusRequestDto dto);

    @Mapping(target = "enrollment", ignore = true)
    @Mapping(target = "fee", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(StudentFeeStatusRequestDto dto, @MappingTarget StudentFeeStatus entity);

    @Mapping(source = "enrollment.enrollmentId", target = "enrollmentId")
    @Mapping(source = "fee.feeId", target = "feeId")
    @Mapping(source = "updatedBy.teacherId", target = "updatedById")
    StudentFeeStatusResponseDto toResponse(StudentFeeStatus entity);

    List<StudentFeeStatusResponseDto> toResponseList(List<StudentFeeStatus> entities);
}
