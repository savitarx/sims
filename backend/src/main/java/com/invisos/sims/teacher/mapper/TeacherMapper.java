package com.invisos.sims.teacher.mapper;

import com.invisos.sims.auth.model.Users;
import com.invisos.sims.teacher.dto.TeachersRequestDto;
import com.invisos.sims.teacher.dto.TeachersResponseDto;
import com.invisos.sims.teacher.model.Teachers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.loginId", target = "email")
    @Mapping(source = "user.role", target = "role")
    TeachersResponseDto toResponseDto(Teachers teacher);


    @Mapping(target = "status",constant = "ACTIVE")
    @Mapping(target = "user",source = "user")
    Teachers toEntity(TeachersRequestDto teachersRequestDto, Users user);

}
