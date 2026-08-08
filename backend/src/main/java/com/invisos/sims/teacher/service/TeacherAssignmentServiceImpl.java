package com.invisos.sims.teacher.service;


import com.invisos.sims.academic.model.AcademicYears;
import com.invisos.sims.academic.model.Sections;
import com.invisos.sims.academic.model.Subjects;
import com.invisos.sims.academic.service.AcademicYearsService;
import com.invisos.sims.academic.service.SectionsService;
import com.invisos.sims.academic.service.SubjectsService;
import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.admin.service.AdminStaffService;
import com.invisos.sims.common.enums.UserStatus;
import com.invisos.sims.common.exception.ResourceAlreadyExistsException;
import com.invisos.sims.common.exception.ResourceNotFoundException;
import com.invisos.sims.teacher.dto.TeachersAssignmentRequestDto;
import com.invisos.sims.teacher.dto.TeachersAssignmentResponseDto;
import com.invisos.sims.teacher.dto.TeachersResponseDto;
import com.invisos.sims.teacher.mapper.TeacherMapper;
import com.invisos.sims.teacher.mapper.TeachersAssignmentMapper;

import com.invisos.sims.teacher.model.TeacherAssignment;
import com.invisos.sims.teacher.model.Teachers;
import com.invisos.sims.teacher.repository.TeacherAssignmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TeacherAssignmentServiceImpl implements TeacherAssignmentService {

    private final TeacherAssignmentRepository teacherAssignmentRepository;

    private final TeachersService teachersService;
    private final AcademicYearsService academicYearsService;
    private final SectionsService sectionsService;
    private final SubjectsService subjectsService;
    private final AdminStaffService adminStaffService;
    private final TeachersAssignmentMapper mapper;
    private final TeacherMapper teacherMapper;

    public TeacherAssignmentServiceImpl(TeacherAssignmentRepository teacherAssignmentRepository, TeachersService teachersService, AcademicYearsService academicYearsService, SectionsService sectionsService, SubjectsService subjectsService, AdminStaffService adminStaffService, TeachersAssignmentMapper mapper, TeacherMapper teacherMapper) {
        this.teacherAssignmentRepository = teacherAssignmentRepository;
        this.teachersService = teachersService;
        this.academicYearsService = academicYearsService;
        this.sectionsService = sectionsService;
        this.subjectsService = subjectsService;
        this.adminStaffService = adminStaffService;
        this.mapper = mapper;
        this.teacherMapper = teacherMapper;
    }



    @Override
    @Transactional(readOnly = true)
    public List<TeachersAssignmentResponseDto> findAll() {
        return mapper.toResponseDtoList(teacherAssignmentRepository.findByTeacherStatus(UserStatus.ACTIVE));
    }

    @Override
    @Transactional(readOnly = true)
    public TeachersAssignmentResponseDto findById(UUID id) {
        return mapper.toResponseDto(getTeacherAssignmentEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherAssignment getTeacherAssignmentEntity(UUID id){
        TeacherAssignment assignment = teacherAssignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Teacher Assignment not found for id " + id));

        if (assignment.getTeacher().getStatus() != UserStatus.ACTIVE) {
            throw new ResourceNotFoundException(
                    "Teacher Assignment not found for id " + id);
        }

        return assignment;
    }


    @Override
    @Transactional
    public TeachersAssignmentResponseDto create(TeachersAssignmentRequestDto dto) {


        Teachers teacher = teachersService.getActiveTeacherEntity(dto.getTeacherId());

        AcademicYears academicYear = academicYearsService.findById(dto.getAcademicYearId());

        Sections section = sectionsService.findById(dto.getSectionId());

        Subjects subject = subjectsService.findById(dto.getSubjectId());

        AdminStaff assignedBy = adminStaffService.findById(dto.getAssignedById());



        TeacherAssignment assignment = mapper.toEntity(teacher, academicYear, section, subject, assignedBy);

        if (teacherAssignmentRepository.existsByTeacherAndAcademicYearAndSectionAndSubject(
                teacher, academicYear, section, subject)) {

            throw new ResourceAlreadyExistsException(
                    "Teacher is already assigned to this subject, section, and academic year.");
        }


        try {
            TeacherAssignment saved = teacherAssignmentRepository.save(assignment);
            return mapper.toResponseDto(saved);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistsException(
                    "This teacher is already assigned to this section/subject for the given academic year.");
        }
    }

    @Override
    @Transactional
    public TeachersAssignmentResponseDto update(UUID id, TeachersAssignmentRequestDto dto) {

        TeacherAssignment existingAssignment = getTeacherAssignmentEntity(id);

        Teachers teacher = teachersService.getActiveTeacherEntity(dto.getTeacherId());

        AcademicYears academicYear = academicYearsService.findById(dto.getAcademicYearId());

        Sections section = sectionsService.findById(dto.getSectionId());

        Subjects subject = subjectsService.findById(dto.getSubjectId());

        AdminStaff assignedBy = adminStaffService.findById(dto.getAssignedById());

        existingAssignment.setTeacher(teacher);
        existingAssignment.setAcademicYear(academicYear);
        existingAssignment.setSection(section);
        existingAssignment.setSubject(subject);
        existingAssignment.setAssignedBy(assignedBy);

        try {
            TeacherAssignment saved = teacherAssignmentRepository.save(existingAssignment);
            return mapper.toResponseDto(saved);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistsException(
                    "This teacher is already assigned to this section/subject for the given academic year.");
        }

    }

    @Override
    public void delete(UUID id) {
        TeacherAssignment existingAssignment =
                getTeacherAssignmentEntity(id);

        teacherAssignmentRepository.delete(existingAssignment);

        log.info("Teacher assignment deleted with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeachersAssignmentResponseDto> findByTeacher(UUID teacherId) {
        teachersService.getActiveTeacherEntity(teacherId);
        return mapper.toResponseDtoList(teacherAssignmentRepository.findByTeacher_TeacherId(teacherId));

    }

    @Override
    @Transactional(readOnly = true)
    public List<TeachersAssignmentResponseDto> findBySection(UUID sectionId) {

        sectionsService.findById(sectionId);

        return mapper.toResponseDtoList(
                teacherAssignmentRepository
                        .findBySection_SectionIdAndTeacher_Status(
                                sectionId,
                                UserStatus.ACTIVE
                        )
        );
    }


}
