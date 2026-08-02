package com.invisos.sims.communication.mapper;

import com.invisos.sims.communication.dto.request.AnnouncementRequestDto;
import com.invisos.sims.communication.dto.response.AnnouncementResponseDto;
import com.invisos.sims.communication.model.Announcements;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnnouncementMapper {

    @Mapping(target = "announcementId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Announcements toEntity(AnnouncementRequestDto dto);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(AnnouncementRequestDto dto, @MappingTarget Announcements entity);

    @Mapping(source = "createdBy.adminId", target = "createdById")
    AnnouncementResponseDto toResponse(Announcements entity);

    List<AnnouncementResponseDto> toResponseList(List<Announcements> entities);
}
