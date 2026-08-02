package com.invisos.sims.teacher.service;

import com.invisos.sims.auth.dto.UsersRequestDto;
import com.invisos.sims.auth.model.Users;
import com.invisos.sims.auth.service.UsersService;
import com.invisos.sims.common.enums.UserRole;
import com.invisos.sims.common.enums.UserStatus;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.common.exception.UserAlreadyExistsException;
//import com.invisos.sims.common.storage.S3Service;
import com.invisos.sims.teacher.dto.TeachersCountResponseDto;
import com.invisos.sims.teacher.dto.TeachersRequestDto;
import com.invisos.sims.teacher.dto.TeachersResponseDto;
import com.invisos.sims.teacher.mapper.TeacherMapper;
import com.invisos.sims.teacher.model.Teachers;
import com.invisos.sims.teacher.repository.TeachersRepository;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
public class TeachersServiceImpl implements TeachersService {

    private final TeachersRepository teachersRepository;

    private final UsersService usersService;

//    private final S3Service s3Service;

    private final TeacherMapper teacherMapper;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/webp");


    public TeachersServiceImpl(TeachersRepository teachersRepository, UsersService usersService,  TeacherMapper teacherMapper) {
        this.teachersRepository = teachersRepository;
        this.usersService = usersService;
//        this.s3Service = s3Service;

        this.teacherMapper = teacherMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeachersResponseDto> findAll(Boolean active) {

        List<Teachers> teachers;

        if (active == null) {
            log.info("Fetching all teachers (active and inactive).");
            teachers = teachersRepository.findAll();
        } else {
            UserStatus status = active ? UserStatus.ACTIVE : UserStatus.INACTIVE;
            log.info("Fetching teachers with status: {}", status);
            teachers = teachersRepository.findByStatus(status);
        }

        log.info("Retrieved {} teacher(s).", teachers.size());

        return teacherMapper.toResponseDtoList(teachers);
    }

    @Override
    @Transactional(readOnly = true)
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
        existingTeacher.setPhotoKey(dto.getPhotoKey());
        existingTeacher.setDesignation(dto.getDesignation());

        Teachers updatedTeacher = teachersRepository.save(existingTeacher);

        log.info("Teacher with ID {} updated successfully.", id);

        return teacherMapper.toResponseDto(updatedTeacher);
    }

    @Override
    @Transactional
    public void delete(UUID teacherId) {

        log.info("Deleting (deactivating) teacher with ID: {}", teacherId);
        updateStatus(teacherId, false);

    }

    @Override
    @Transactional(readOnly = true)
    public List<TeachersResponseDto> search(String query) {

        log.info("Searching teachers with query: {}", query);

        List<Teachers> teachers = teachersRepository.searchTeachers(query);

        log.info("Found {} teacher(s) matching query '{}'.",
                teachers.size(), query);

        return teacherMapper.toResponseDtoList(teachers);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeachersResponseDto> findBySubject(String subject) {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public TeachersCountResponseDto getCount() {

        log.info("Fetching teacher count summary.");

        long total = teachersRepository.count();
        long active = teachersRepository.countByStatus(UserStatus.ACTIVE);
        long inactive = teachersRepository.countByStatus(UserStatus.INACTIVE);

        log.info(
                "Teacher count summary - Total: {}, Active: {}, Inactive: {}",
                total, active, inactive
        );

        return TeachersCountResponseDto.builder()
                .totalTeachers(total)
                .activeTeachers(active)
                .inactiveTeachers(inactive)
                .build();
    }

    @Override
    @Transactional
    public List<TeachersResponseDto> createBulk(List<TeachersRequestDto> teacherDtos) {

        log.info("Starting bulk teacher creation. Total records: {}", teacherDtos.size());

        if (teacherDtos.isEmpty()) {
            throw new IllegalArgumentException("Teacher list cannot be empty.");
        }

        if (teacherDtos.size() > 1000) {
            throw new IllegalArgumentException("Maximum 1000 teachers can be imported at once.");
        }

        Set<String> employeeIds = new HashSet<>();
        Set<String> emails = new HashSet<>();

        for (TeachersRequestDto dto : teacherDtos) {
            if (!employeeIds.add(dto.getEmployeeId())) {
                throw new UserAlreadyExistsException("Duplicate Employee ID in request: " + dto.getEmployeeId());
            }
            if (!emails.add(dto.getEmail())) {
                throw new UserAlreadyExistsException("Duplicate Email in request: " + dto.getEmail());
            }
        }

        List<Teachers> existingTeachers = teachersRepository.findByEmployeeIdIn(employeeIds);
        if (!existingTeachers.isEmpty()) {
            List<String> duplicates = existingTeachers.stream().map(Teachers::getEmployeeId).toList();
            throw new UserAlreadyExistsException("Employee IDs already exist: " + duplicates);
        }

        List<Users> existingUsers = usersService.findByEmailIn(emails);
        if (!existingUsers.isEmpty()) {
            List<String> duplicateEmails = existingUsers.stream().map(Users::getEmail).toList();
            throw new UserAlreadyExistsException("Emails already exist: " + duplicateEmails);
        }

        // build all entities first, then batch-insert in one call
        List<Teachers> teachersToSave = new ArrayList<>();


        for (TeachersRequestDto dto : teacherDtos) {

            UsersRequestDto userDto = new UsersRequestDto();
            userDto.setLoginId(dto.getEmployeeId());
            userDto.setEmail(dto.getEmail());
            userDto.setRole(UserRole.TEACHER);
            userDto.setPassword(generatePassword(dto.getEmployeeId(), dto.getContact()));

            Users newUser = usersService.create(userDto); // still one insert per user

            Teachers teacher = teacherMapper.toEntity(dto, newUser);
            teachersToSave.add(teacher);
        }

        List<Teachers> savedTeachers = teachersRepository.saveAll(teachersToSave); // ONE batched insert call

        List<TeachersResponseDto> response = savedTeachers.stream()
                .map(teacherMapper::toResponseDto)
                .toList();

        log.info("Bulk teacher creation completed successfully. {} teachers created.", response.size());

        return response;
    }

    @Override
    @Transactional
    public TeachersResponseDto updateStatus(UUID id, boolean active) {

        log.info("Updating status for teacher ID: {} to {}", id, active ? "ACTIVE" : "INACTIVE");

        Teachers teacher = teachersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with the given id " + id));

        UserStatus targetStatus = active ? UserStatus.ACTIVE : UserStatus.INACTIVE;

        if (teacher.getStatus() == targetStatus) {
            log.warn("Teacher with ID {} is already {}.", id, targetStatus);
            throw new IllegalStateException("Teacher is already " + targetStatus.name().toLowerCase() + ".");
        }

        // TODO: if deactivating, check if teacher is assigned as class teacher,
        // subject teacher or referenced in any class timetable (same as delete()).

        if (active) {
            usersService.restore(teacher.getUser().getUserId()); // reactivate linked user account
        } else {
            usersService.delete(teacher.getUser().getUserId()); // deactivate linked user account
        }

        teacher.setStatus(targetStatus);
        Teachers saved = teachersRepository.save(teacher);

        log.info("Teacher with ID {} status updated to {}.", id, targetStatus);

        return teacherMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public TeachersResponseDto restore(UUID id) {
        return updateStatus(id, true);
    }


//
//    @Override
//    @Transactional
//    public TeachersResponseDto uploadPhoto(UUID id, MultipartFile file) {
//
//        log.info("Uploading photo for teacher ID: {}", id);
//
//        Teachers teacher = teachersRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with the given id " + id));
//
//        if (teacher.getStatus() == UserStatus.INACTIVE) {
//            throw new IllegalStateException("Cannot upload photo for an inactive teacher.");
//        }
//
//        validatePhoto(file);
//
//        String oldKey = teacher.getPhotoKey(); // null on first upload
//
//        String extension = getExtension(file.getOriginalFilename());
//        String newKey = "teachers/" + id + "/" + UUID.randomUUID() + extension;
//
//        String savedKey = s3Service.upload(newKey, file);
//
//        teacher.setPhotoKey(savedKey);
//        Teachers saved = teachersRepository.save(teacher);
//
//        // clean up old photo AFTER successful DB save, so a failed save doesn't orphan the new upload
//        // while leaving the DB pointing at a deleted old one
//        if (oldKey != null) {
//            s3Service.delete(oldKey);
//        }
//
//        log.info("Photo uploaded successfully for teacher ID: {}", id);
//
//        return teacherMapper.toResponseDto(saved); // mapper generates presigned URL — see Step 8
//    }
//
//    private void validatePhoto(MultipartFile file) {
//        if (file == null || file.isEmpty()) {
//            throw new IllegalArgumentException("Photo file is required.");
//        }
//        if (file.getSize() > MAX_FILE_SIZE) {
//            throw new IllegalArgumentException("Photo must be under 5MB.");
//        }
//        if (!ALLOWED_TYPES.contains(file.getContentType())) {
//            throw new IllegalArgumentException("Only JPEG, PNG, or WEBP images are allowed.");
//        }
//    }
//
//    private String getExtension(String filename) {
//        if (filename == null || !filename.contains(".")) return "";
//        return filename.substring(filename.lastIndexOf("."));
//    }

}
