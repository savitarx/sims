package com.invisos.sims.auth.service;

import com.invisos.sims.auth.dto.UsersRequestDto;
import com.invisos.sims.auth.model.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public interface UsersService {

    List<Users> findAll();

    Users findById(UUID id);

    Users create(UsersRequestDto entity);

    Users update(UUID id, Users entity);

    void delete(UUID id);

    void updateEmail(UUID userId, String email);
}
