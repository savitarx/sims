package com.invisos.sims.teacher.controller;

import com.invisos.sims.teacher.dto.TeachersCountResponseDto;
import com.invisos.sims.teacher.dto.TeachersRequestDto;
import com.invisos.sims.teacher.dto.TeachersResponseDto;

import com.invisos.sims.teacher.service.TeachersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeachersController {

    private final TeachersService teachersService;


    public TeachersController(TeachersService teachersService) {
        this.teachersService = teachersService;

    }

    //    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    // TODO: CREATE AN SEPARATE ENDPOINT SPECIFIC TO USER SPECIFIC TO RETRIEVE ONLY ACTIVE
    //NORMAL USERS -> ACTIVE , ADMIN -> INACTIVE
    @GetMapping
    public ResponseEntity<List<TeachersResponseDto>> getAll(
            @RequestParam(required = false) UUID subjectId,
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.status(HttpStatus.OK).body(teachersService.findAll(subjectId,active));
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

    @PatchMapping("/{id}/status")
    public ResponseEntity<TeachersResponseDto> updateStatus(
            @PathVariable UUID id,
            @RequestParam boolean active) {
        return ResponseEntity.ok(teachersService.updateStatus(id, active));
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<TeachersResponseDto> restore(@PathVariable UUID id) {
        return ResponseEntity.ok(teachersService.restore(id));
    }

//    @PutMapping("/{id}/photo")
//    public ResponseEntity<TeachersResponseDto> uploadPhoto(
//            @PathVariable UUID id,
//            @RequestParam("file") MultipartFile file) {
//        return ResponseEntity.ok(teachersService.uploadPhoto(id, file));
//    }


    @GetMapping("/search")
    public ResponseEntity<List<TeachersResponseDto>> search(@RequestParam String query) {
        return ResponseEntity.status(HttpStatus.OK).body(teachersService.search(query));
    }

    @GetMapping("/count")
    public ResponseEntity<TeachersCountResponseDto> count() {
        return ResponseEntity.status(HttpStatus.OK).body(teachersService.getCount());
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<TeachersResponseDto>> createBulk(
            @RequestBody  List< @Valid  TeachersRequestDto> teachersRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teachersService.createBulk(teachersRequest));
    }


}
