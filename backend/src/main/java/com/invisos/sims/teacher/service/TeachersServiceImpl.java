package com.invisos.sims.teacher.service;

import com.invisos.sims.auth.dto.UsersRequestDto;
import com.invisos.sims.auth.model.Users;
import com.invisos.sims.auth.service.UsersService;
import com.invisos.sims.common.enums.UserRole;
import com.invisos.sims.common.enums.UserStatus;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.common.exception.UserAlreadyExistsException;
import com.invisos.sims.teacher.dto.TeachersRequestDto;
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
    public List<Teachers> findAll() {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Teachers findById(UUID id) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    @Transactional
    public Teachers create(TeachersRequestDto teachersRequestDto) {

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

        return savedTeacher;
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
    public Teachers update(UUID id, Teachers entity) {
        // TODO: implement
        throw new UnsupportedOperationException("TODO");
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
