package com.invisos.sims.auth.service;

import com.invisos.sims.auth.dto.UsersRequestDto;
import com.invisos.sims.auth.model.Users;
import com.invisos.sims.auth.repository.UsersRepository;
import com.invisos.sims.common.enums.UserStatus;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.common.exception.UserAlreadyExistsException;
import io.jsonwebtoken.security.Password;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Users> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Users findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Users create(UsersRequestDto usersRequestDto) {
        if (usersRepository.existsByLoginId(usersRequestDto.getLoginId())) {
            throw new UserAlreadyExistsException(
                    "User with login ID '" + usersRequestDto.getLoginId() + "' already exists."
            );
        }

        Users newUser = new Users();
        newUser.setLoginId(usersRequestDto.getLoginId());
        newUser.setEmail(usersRequestDto.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(usersRequestDto.getPassword()));
        newUser.setStatus(UserStatus.ACTIVE);
        newUser.setRole(usersRequestDto.getRole());

        return usersRepository.save(newUser);
    }

    @Override
    public Users update(UUID id, Users entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        log.info("Attempting to deactivate user with ID: {}", userId);

        Users existingUser = usersRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with the given ID: " + userId));

        if (existingUser.getStatus() == UserStatus.INACTIVE) {

            log.warn("User with ID {} is already inactive.", userId);

            throw new IllegalStateException(
                    "User is already inactive.");
        }

        existingUser.setStatus(UserStatus.INACTIVE);

        usersRepository.save(existingUser);

        log.info("User with ID {} has been marked as inactive.", userId);


    }

    @Override
    public void updateEmail(UUID userId, String email) {

    }


}
