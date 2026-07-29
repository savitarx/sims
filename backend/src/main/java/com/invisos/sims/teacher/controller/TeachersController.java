package com.invisos.sims.teacher.controller;

import com.invisos.sims.teacher.dto.TeachersRequestDto;
import com.invisos.sims.teacher.dto.TeachersResponseDto;
import com.invisos.sims.teacher.mapper.TeacherMapper;
import com.invisos.sims.teacher.model.Teachers;
import com.invisos.sims.teacher.service.TeachersService;
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
    @GetMapping
    public ResponseEntity<List<Teachers>> getAll() {
        return ResponseEntity.ok(teachersService.findAll());
    }

    //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping("/{id}")
    public ResponseEntity<Teachers> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(teachersService.findById(id));
    }

    //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PostMapping
    public ResponseEntity<TeachersResponseDto> create(@RequestBody TeachersRequestDto teacher) {

        Teachers newTeacher = teachersService.create(teacher);
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherMapper.toResponseDto(newTeacher));
    }

    //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PutMapping("/{id}")
    public ResponseEntity<Teachers> update(@PathVariable UUID id, @RequestBody Teachers teacher) {
        return ResponseEntity.ok(teachersService.update(id, teacher));
    }

    //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        teachersService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
