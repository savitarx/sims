package com.invisos.sims.admin.controller;

import com.invisos.sims.admin.model.AdminStaff;
import com.invisos.sims.admin.service.AdminStaffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin-staff")
public class AdminStaffController {

    private final AdminStaffService adminStaffService;

    public AdminStaffController(AdminStaffService adminStaffService) {
        this.adminStaffService = adminStaffService;
    }

    @GetMapping
    public ResponseEntity<List<AdminStaff>> getAll() {
        return ResponseEntity.ok(adminStaffService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminStaff> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminStaffService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AdminStaff> create(@RequestBody AdminStaff entity) {
        return ResponseEntity.ok(adminStaffService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminStaff> update(@PathVariable UUID id, @RequestBody AdminStaff entity) {
        return ResponseEntity.ok(adminStaffService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        adminStaffService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
