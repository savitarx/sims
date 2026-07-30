package com.invisos.sims.teacher.controller;

import com.invisos.sims.teacher.dto.TeachersRequestDto;
import com.invisos.sims.teacher.dto.TeachersResponseDto;
import com.invisos.sims.teacher.mapper.TeacherMapper;
import com.invisos.sims.teacher.model.Teachers;
import com.invisos.sims.teacher.service.TeachersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1/teachers")
public class TeachersController {

    private final TeachersService teachersService;

    private final TeacherMapper teacherMapper;

    public TeachersController(TeachersService teachersService, TeacherMapper teacherMapper) {
        this.teachersService = teachersService;
        this.teacherMapper = teacherMapper;
    }

    //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    //TODO: CREATE AN SEPARATE ENDPOINT SPECIFIC TO USER SPECIFIC TO RETRIVE ONLY ACTIVE
    //NORMAL USERS -> ACTIVE , ADMIN -> INACTIVE
    @GetMapping
    public ResponseEntity<List<TeachersResponseDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(teachersService.findAll());
    }

    //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping("/{id}")
    public ResponseEntity<TeachersResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(teachersService.findById(id));
    }

    //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PostMapping
    public ResponseEntity<TeachersResponseDto> create(@RequestBody @Valid TeachersRequestDto teacher) {

        TeachersResponseDto newTeacher = teachersService.create(teacher);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTeacher);
    }

    //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PutMapping("/{id}")
    public ResponseEntity<TeachersResponseDto> update(@PathVariable UUID id, @RequestBody @Valid TeachersRequestDto teacher) {
        return ResponseEntity.status(HttpStatus.OK).body(teachersService.update(id, teacher));
    }

    //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        teachersService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
