package com.invisos.sims.fee.mapper;

import com.invisos.sims.common.dto.summary.ActorSummaryDto;
import com.invisos.sims.common.mapper.SummaryMapper;
import com.invisos.sims.fee.dto.request.StudentFeeStatusRequestDto;
import com.invisos.sims.fee.dto.response.StudentFeeStatusResponseDto;
import com.invisos.sims.fee.model.StudentFeeStatus;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SummaryMapper.class, FeeMapper.class})
public abstract class StudentFeeStatusMapper {

    /** Used by {@link #resolveActor} to collapse the two actor columns into one. */
    @Autowired
    protected SummaryMapper summaries;

    @Mapping(target = "studentFeeStatusId", ignore = true)
    @Mapping(target = "enrollment", ignore = true)
    @Mapping(target = "fee", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedByAdmin", ignore = true)
    public abstract StudentFeeStatus toEntity(StudentFeeStatusRequestDto dto);

    @Mapping(target = "enrollment", ignore = true)
    @Mapping(target = "fee", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedByAdmin", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract void updateEntity(StudentFeeStatusRequestDto dto,
                                      @MappingTarget StudentFeeStatus entity);

    @Mapping(source = "enrollment", target = "student")
    @Mapping(source = "enrollment.section", target = "section")
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(source = "enrollment.enrollmentId", target = "enrollmentId")
    @Mapping(source = "fee.feeId", target = "feeId")
    @Mapping(source = "updatedBy.teacherId", target = "updatedById")
    public abstract StudentFeeStatusResponseDto toResponse(StudentFeeStatus entity);

    public abstract List<StudentFeeStatusResponseDto> toResponseList(List<StudentFeeStatus> entities);

    /**
     * The status may have been changed by a teacher or by admin staff, held in two
     * nullable columns; the response exposes whichever one is set as a single actor.
     */
    @AfterMapping
    protected void resolveActor(StudentFeeStatus entity,
                                @MappingTarget StudentFeeStatusResponseDto dto) {

        ActorSummaryDto actor = entity.getUpdatedBy() != null
                ? summaries.toActor(entity.getUpdatedBy())
                : summaries.toActor(entity.getUpdatedByAdmin());
        dto.setUpdatedBy(actor);
    }
}
