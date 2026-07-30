package com.invisos.sims.teacher.service;

import com.invisos.sims.auth.dto.UsersRequestDto;
import com.invisos.sims.auth.model.Users;
import com.invisos.sims.auth.service.UsersService;
import com.invisos.sims.common.enums.UserRole;
import com.invisos.sims.common.enums.UserStatus;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.common.exception.UserAlreadyExistsException;
import com.invisos.sims.teacher.dto.TeachersRequestDto;
import com.invisos.sims.teacher.dto.TeachersResponseDto;
import com.invisos.sims.teacher.mapper.TeacherMapper;
import com.invisos.sims.teacher.model.Teachers;
import com.invisos.sims.teacher.repository.TeachersRepository;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TeachersServiceImpl implements TeachersService {

    private final TeachersRepository teachersRepository;

    private final UsersService usersService;

    private final TeacherMapper teacherMapper;


    public TeachersServiceImpl(TeachersRepository teachersRepository, UsersService usersService, TeacherMapper teacherMapper) {
        this.teachersRepository = teachersRepository;
        this.usersService = usersService;

        this.teacherMapper = teacherMapper;
    }

    @Override
    public List<TeachersResponseDto> findAll() {

        log.info("Fetching all active teachers.");

        List<Teachers> teachers =
                teachersRepository.findByStatus(UserStatus.ACTIVE);

        log.info("Retrieved {} active teachers.", teachers.size());


        return teacherMapper.toResponseDtoList(teachers);
    }

    @Override
    public TeachersResponseDto findById(UUID id) {

        log.info("Fetching teacher with ID: {}", id);
        Teachers existingTeacher = teachersRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Teacher not found with the given id " + id)
        );

        if (existingTeacher.getStatus() == UserStatus.INACTIVE) {
            throw new ResourceNotFoundException("Teacher Status is Inactive");
        }

        if (existingTeacher.getUser().getStatus() == UserStatus.INACTIVE) {
            throw new IllegalStateException("Associated user is not active");
        }

        log.info("Successfully retrieved teacher with ID: {}", id);
        return teacherMapper.toResponseDto(existingTeacher);
    }

    @Override
    @Transactional
    public TeachersResponseDto create(TeachersRequestDto teachersRequestDto) {

        log.info("Creating teacher with employee ID: {}",
                teachersRequestDto.getEmployeeId());

        if (teachersRepository.existsByEmployeeId(
                teachersRequestDto.getEmployeeId())) {

            log.warn("Teacher creation failed. Employee ID already exists: {}",
                    teachersRequestDto.getEmployeeId());

            throw new UserAlreadyExistsException(
                    "Employee ID already exists.");
        }

        UsersRequestDto usersRequestDto = new UsersRequestDto();
        usersRequestDto.setLoginId(teachersRequestDto.getEmployeeId());
        usersRequestDto.setPassword(
                generatePassword(
                        teachersRequestDto.getEmployeeId(),
                        teachersRequestDto.getContact()
                )
        );
        usersRequestDto.setEmail(teachersRequestDto.getEmail());
        usersRequestDto.setRole(UserRole.TEACHER);

        Users newUser = usersService.create(usersRequestDto);

        log.info("User created successfully with user ID: {}",
                newUser.getUserId());

        Teachers newTeacher =
                teacherMapper.toEntity(teachersRequestDto, newUser);

        Teachers savedTeacher = teachersRepository.save(newTeacher);

        log.info("Teacher created successfully with teacher ID: {}",
                savedTeacher.getTeacherId());

        return teacherMapper.toResponseDto(savedTeacher);
    }


    private String generatePassword(String employeeId, String contact) {


        if (employeeId == null || employeeId.isBlank()) {
            log.warn("Password generation failed. Employee ID is null or empty.");
            throw new IllegalArgumentException("Employee ID cannot be empty.");
        }


        if (contact == null || contact.length() < 4) {
            log.warn(
                    "Password generation failed. Invalid contact number provided for employee ID: {}",
                    employeeId
            );

            throw new IllegalArgumentException(
                    "Contact number must contain at least 4 digits."
            );
        }

        // Generate the temporary password using the employee ID and
        // the last four digits of the contact number.
        String password = employeeId + "@"
                + contact.substring(contact.length() - 4);

        // Do not log the generated password for security reasons.
        log.debug(
                "Temporary password generated successfully for employee ID: {}",
                employeeId
        );

        return password;
    }

    @Override
    @Transactional
    public TeachersResponseDto update(UUID id, TeachersRequestDto dto) {

        log.info("Updating teacher with ID: {}", id);

        Teachers existingTeacher = teachersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with the given id " + id));

        if (existingTeacher.getStatus() == UserStatus.INACTIVE) {
            throw new IllegalStateException("Cannot update an inactive teacher.");
        }

        if (existingTeacher.getUser().getStatus() == UserStatus.INACTIVE) {
            throw new IllegalStateException("Associated user is not active");
        }

        // employeeId uniqueness check, excluding self
        if (!existingTeacher.getEmployeeId().equals(dto.getEmployeeId())
                && teachersRepository.existsByEmployeeIdAndTeacherIdNot(dto.getEmployeeId(), id)) {
            log.warn("Teacher update failed. Employee ID already exists: {}", dto.getEmployeeId());
            throw new UserAlreadyExistsException("Employee ID already exists.");
        }

        // sync email to Users if changed
        if (!existingTeacher.getUser().getEmail().equals(dto.getEmail())) {
            usersService.updateEmail(existingTeacher.getUser().getUserId(), dto.getEmail());
        }

        existingTeacher.setEmployeeId(dto.getEmployeeId());
        existingTeacher.setName(dto.getName());
        existingTeacher.setParentName(dto.getParentName());
        existingTeacher.setContact(dto.getContact());
        existingTeacher.setQualification(dto.getQualification());
        existingTeacher.setJoiningDate(dto.getJoiningDate());
        existingTeacher.setPhotoUrl(dto.getPhotoUrl());
        existingTeacher.setDesignation(dto.getDesignation());

        Teachers updatedTeacher = teachersRepository.save(existingTeacher);

        log.info("Teacher with ID {} updated successfully.", id);

        return teacherMapper.toResponseDto(updatedTeacher);
    }

    @Override
    @Transactional
    public void delete(UUID teacherId) {

        log.info("Deleting teacher with ID: {}", teacherId);

        Teachers teacher = teachersRepository.findById(teacherId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found with the given id. " + teacherId));

        if (teacher.getStatus() == UserStatus.INACTIVE) {
            log.warn("Teacher with ID {} is already inactive.", teacherId);
            throw new IllegalStateException("Teacher is already inactive.");
        }


        // TODO:
        // Check if the teacher is assigned as class teacher,
        // subject teacher or referenced in any class timetable.

        log.info("Deactivating associated user account for teacher ID: {}", teacherId);
        usersService.delete(teacher.getUser().getUserId());

        // Soft delete the teacher.
        teacher.setStatus(UserStatus.INACTIVE);

        teachersRepository.save(teacher);

        log.info("Teacher with ID {} has been marked as inactive.", teacherId);

    }


}
