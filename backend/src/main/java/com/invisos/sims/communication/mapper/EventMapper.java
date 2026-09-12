package com.invisos.sims.communication.mapper;

import com.invisos.sims.common.mapper.SummaryMapper;
import com.invisos.sims.communication.dto.request.EventRequestDto;
import com.invisos.sims.communication.dto.response.EventResponseDto;
import com.invisos.sims.communication.model.Events;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = SummaryMapper.class)
public interface EventMapper {

    @Mapping(target = "eventId", ignore = true)
    @Mapping(target = "schoolClass", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Events toEntity(EventRequestDto dto);

    @Mapping(target = "schoolClass", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(EventRequestDto dto, @MappingTarget Events entity);

    @Mapping(target = "schoolWide", ignore = true)
    @Mapping(source = "schoolClass.classId", target = "classId")
    @Mapping(source = "createdBy.adminId", target = "createdById")
    EventResponseDto toResponse(Events entity);

    List<EventResponseDto> toResponseList(List<Events> entities);

    /** A null class is the marker for a school-wide event. */
    @AfterMapping
    default void flagSchoolWide(Events entity, @MappingTarget EventResponseDto dto) {
        dto.setSchoolWide(entity.getSchoolClass() == null);
    }
}
