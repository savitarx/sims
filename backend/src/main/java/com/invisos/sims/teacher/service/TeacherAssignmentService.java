package com.invisos.sims.teacher.service;

import com.invisos.sims.teacher.dto.TeachersAssignmentRequestDto;
import com.invisos.sims.teacher.dto.TeachersAssignmentResponseDto;
import com.invisos.sims.teacher.dto.TeachersResponseDto;
import com.invisos.sims.teacher.model.TeacherAssignment;

import java.util.List;
import java.util.UUID;

public interface TeacherAssignmentService {

    List<TeachersAssignmentResponseDto> findAll();
    TeachersAssignmentResponseDto findById(UUID id);
    TeachersAssignmentResponseDto create(TeachersAssignmentRequestDto dto);
    TeachersAssignmentResponseDto update(UUID id, TeachersAssignmentRequestDto dto);
    TeacherAssignment getTeacherAssignmentEntity(UUID id);
    void delete(UUID id);
    List<TeachersAssignmentResponseDto> findByTeacher(UUID teacherId);
    List<TeachersAssignmentResponseDto> findBySection(UUID sectionId);

}
