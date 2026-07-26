package com.invisos.sims.student.controller;

import com.invisos.sims.student.dto.StudentRequestDto;
import com.invisos.sims.student.dto.StudentResponseDto;
import com.invisos.sims.student.mapper.StudentMapper;
import com.invisos.sims.student.model.Students;
import com.invisos.sims.student.service.StudentsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
public class StudentsController {

    private final StudentsService studentsService;
    private final StudentMapper studentMapper;

    public StudentsController(StudentsService studentsService, StudentMapper studentMapper) {
        this.studentsService = studentsService;
        this.studentMapper = studentMapper;
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping("/test")
    public String test() {
        return "Test Api!!";
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getAll() {
        return ResponseEntity.ok(studentMapper.toResponseList(studentsService.findAll()));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentMapper.toResponse(studentsService.findById(id)));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PostMapping
    public ResponseEntity<StudentResponseDto> create(@Valid @RequestBody StudentRequestDto request) {
        Students created = studentsService.create(studentMapper.toEntity(request));
        return ResponseEntity.ok(studentMapper.toResponse(created));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> update(@PathVariable UUID id,
                                                     @Valid @RequestBody StudentRequestDto request) {
        Students updated = studentsService.update(id, studentMapper.toEntity(request));
        return ResponseEntity.ok(studentMapper.toResponse(updated));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        studentsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
