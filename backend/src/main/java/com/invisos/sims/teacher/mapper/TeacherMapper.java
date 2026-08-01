package com.invisos.sims.teacher.mapper;

import com.invisos.sims.auth.model.Users;
import com.invisos.sims.teacher.dto.TeachersRequestDto;
import com.invisos.sims.teacher.dto.TeachersResponseDto;
import com.invisos.sims.teacher.model.Teachers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.role", target = "role")
    @Mapping(source="parentName",target="parentName")
    TeachersResponseDto toResponseDto(Teachers teacher);

    List<TeachersResponseDto> toResponseDtoList(List<Teachers> teachers);


    @Mapping(target = "status",constant = "ACTIVE")
    @Mapping(target = "user",source = "user")
    Teachers toEntity(TeachersRequestDto teachersRequestDto, Users user);

}
