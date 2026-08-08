package com.invisos.sims.teacher.controller;


import com.invisos.sims.teacher.dto.TeachersAssignmentRequestDto;
import com.invisos.sims.teacher.dto.TeachersAssignmentResponseDto;
import com.invisos.sims.teacher.dto.TeachersResponseDto;
import com.invisos.sims.teacher.service.TeacherAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teacher-assignments")
public class TeacherAssignmentController {


        private final TeacherAssignmentService teacherAssignmentService;

        public TeacherAssignmentController(TeacherAssignmentService teacherAssignmentService) {
            this.teacherAssignmentService = teacherAssignmentService;
        }

        //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
        @GetMapping
        public ResponseEntity<List<TeachersAssignmentResponseDto>> getAll() {
            return ResponseEntity.ok(teacherAssignmentService.findAll());
        }

        //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
        @GetMapping("/{id}")
        public ResponseEntity<TeachersAssignmentResponseDto> getById(@PathVariable UUID id) {
            return ResponseEntity.ok(teacherAssignmentService.findById(id));
        }

        //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
        @PostMapping
        public ResponseEntity<TeachersAssignmentResponseDto> create(
                @RequestBody @Valid TeachersAssignmentRequestDto dto) {
            TeachersAssignmentResponseDto created = teacherAssignmentService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }

        //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
        @PutMapping("/{id}")
        public ResponseEntity<TeachersAssignmentResponseDto> update(
                @PathVariable UUID id, @RequestBody @Valid TeachersAssignmentRequestDto dto) {
            return ResponseEntity.ok(teacherAssignmentService.update(id, dto));
        }

        //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable UUID id) {
            teacherAssignmentService.delete(id);
            return ResponseEntity.noContent().build();
        }



        //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
        @GetMapping("/by-teacher/{teacherId}")
        public ResponseEntity<List<TeachersAssignmentResponseDto>> getByTeacher(@PathVariable UUID teacherId) {
            return ResponseEntity.ok(teacherAssignmentService.findByTeacher(teacherId));
        }

        //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
        @GetMapping("/by-section/{sectionId}")
        public ResponseEntity<List<TeachersAssignmentResponseDto>> getBySection(@PathVariable UUID sectionId) {
            return ResponseEntity.ok(teacherAssignmentService.findBySection(sectionId));
        }
    }

