package com.invisos.sims.exam.mapper;

import com.invisos.sims.exam.dto.request.MarkRequestDto;
import com.invisos.sims.exam.dto.response.MarkResponseDto;
import com.invisos.sims.exam.model.Marks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MarkMapper {

    @Mapping(target = "markId", ignore = true)
    @Mapping(target = "enrollment", ignore = true)
    @Mapping(target = "examSubject", ignore = true)
    @Mapping(target = "enteredBy", ignore = true)
    Marks toEntity(MarkRequestDto dto);

    @Mapping(target = "enrollment", ignore = true)
    @Mapping(target = "examSubject", ignore = true)
    @Mapping(target = "enteredBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(MarkRequestDto dto, @MappingTarget Marks entity);

    @Mapping(source = "enrollment.enrollmentId", target = "enrollmentId")
    @Mapping(source = "examSubject.examSubjectId", target = "examSubjectId")
    @Mapping(source = "enteredBy.teacherId", target = "enteredById")
    MarkResponseDto toResponse(Marks entity);

    List<MarkResponseDto> toResponseList(List<Marks> entities);
}
