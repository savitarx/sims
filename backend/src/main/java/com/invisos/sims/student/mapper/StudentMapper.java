package com.invisos.sims.student.mapper;

import com.invisos.sims.student.dto.StudentRequestDto;
import com.invisos.sims.student.dto.StudentResponseDto;
import com.invisos.sims.student.model.Students;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * MapStruct mapper between {@link Students} and its DTOs. This is the reference
 * pattern each module should replicate for its own entities.
 *
 * <p>Associations ({@code user}, {@code createdBy}) are intentionally left
 * unmapped on the inbound side — the service resolves ids into managed entities.
 */
@Mapper(componentModel = "spring")
public interface StudentMapper {

    // createdAt/updatedAt are inherited from BaseEntity and are not part of the
    // Lombok @Builder, so they are neither mapped nor ignorable here.
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Students toEntity(StudentRequestDto dto);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(StudentRequestDto dto, @MappingTarget Students entity);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "createdBy.adminId", target = "createdById")
    StudentResponseDto toResponse(Students entity);

    List<StudentResponseDto> toResponseList(List<Students> entities);
}
