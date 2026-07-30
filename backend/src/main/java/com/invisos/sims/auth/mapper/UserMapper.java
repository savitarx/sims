package com.invisos.sims.auth.mapper;

import com.invisos.sims.auth.dto.UsersRequestDto;
import com.invisos.sims.auth.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "password",target = "passwordHash")
    @Mapping(target = "status",constant = "ACTIVE")
    Users toEntity(UsersRequestDto usersRequestDto);
}
