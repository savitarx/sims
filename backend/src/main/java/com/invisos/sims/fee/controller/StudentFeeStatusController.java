package com.invisos.sims.fee.controller;

import com.invisos.sims.fee.dto.request.StudentFeeStatusRequestDto;
import com.invisos.sims.fee.dto.response.StudentFeeStatusResponseDto;
import com.invisos.sims.fee.mapper.StudentFeeStatusMapper;
import com.invisos.sims.fee.model.StudentFeeStatus;
import com.invisos.sims.fee.service.StudentFeeStatusService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/student-fee-status")
public class StudentFeeStatusController {

    private final StudentFeeStatusService studentFeeStatusService;
    private final StudentFeeStatusMapper studentFeeStatusMapper;

    public StudentFeeStatusController(StudentFeeStatusService studentFeeStatusService,
                                      StudentFeeStatusMapper studentFeeStatusMapper) {
        this.studentFeeStatusService = studentFeeStatusService;
        this.studentFeeStatusMapper = studentFeeStatusMapper;
    }

    @GetMapping("/test")
    public String test() {
        return "Student Fee Status API Working!";
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER
    @GetMapping
    public ResponseEntity<List<StudentFeeStatusResponseDto>> getAll() {
        return ResponseEntity.ok(studentFeeStatusMapper.toResponseList(studentFeeStatusService.findAll()));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER
    @GetMapping("/{id}")
    public ResponseEntity<StudentFeeStatusResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentFeeStatusMapper.toResponse(studentFeeStatusService.findById(id)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER/STUDENT
    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<List<StudentFeeStatusResponseDto>> getByEnrollmentId(@PathVariable UUID enrollmentId) {
        return ResponseEntity.ok(
                studentFeeStatusMapper.toResponseList(studentFeeStatusService.findByEnrollmentId(enrollmentId)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER
    @PostMapping
    public ResponseEntity<StudentFeeStatusResponseDto> create(
            @Valid @RequestBody StudentFeeStatusRequestDto request) {

        StudentFeeStatus created = studentFeeStatusService.create(request);
        return ResponseEntity.ok(studentFeeStatusMapper.toResponse(created));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER
    @PutMapping("/{id}")
    public ResponseEntity<StudentFeeStatusResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody StudentFeeStatusRequestDto request) {

        StudentFeeStatus updated = studentFeeStatusService.update(id, request);
        return ResponseEntity.ok(studentFeeStatusMapper.toResponse(updated));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        studentFeeStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
