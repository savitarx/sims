package com.invisos.sims.fee.controller;

import com.invisos.sims.fee.model.StudentFeeStatus;
import com.invisos.sims.fee.service.StudentFeeStatusService;
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
@RequestMapping("/api/v1/student-fee-status")
public class StudentFeeStatusController {

    private final StudentFeeStatusService studentFeeStatusService;

    public StudentFeeStatusController(StudentFeeStatusService studentFeeStatusService) {
        this.studentFeeStatusService = studentFeeStatusService;
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping
    public ResponseEntity<List<StudentFeeStatus>> getAll() {
        return ResponseEntity.ok(studentFeeStatusService.findAll());
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping("/{id}")
    public ResponseEntity<StudentFeeStatus> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentFeeStatusService.findById(id));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PostMapping
    public ResponseEntity<StudentFeeStatus> create(@RequestBody StudentFeeStatus entity) {
        return ResponseEntity.ok(studentFeeStatusService.create(entity));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PutMapping("/{id}")
    public ResponseEntity<StudentFeeStatus> update(@PathVariable UUID id, @RequestBody StudentFeeStatus entity) {
        return ResponseEntity.ok(studentFeeStatusService.update(id, entity));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        studentFeeStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
