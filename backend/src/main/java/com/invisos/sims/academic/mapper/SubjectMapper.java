package com.invisos.sims.academic.mapper;

import com.invisos.sims.academic.dto.request.SubjectRequestDto;
import com.invisos.sims.academic.dto.response.SubjectResponseDto;
import com.invisos.sims.academic.model.Subjects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    @Mapping(target = "subjectId", ignore = true)
    Subjects toEntity(SubjectRequestDto dto);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(SubjectRequestDto dto, @MappingTarget Subjects entity);

    SubjectResponseDto toResponse(Subjects entity);

    List<SubjectResponseDto> toResponseList(List<Subjects> entities);
}
