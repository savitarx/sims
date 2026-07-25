package com.invisos.sims.admin.controller;

import com.invisos.sims.admin.model.PrincipalTenure;
import com.invisos.sims.admin.service.PrincipalTenureService;
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
@RequestMapping("/api/v1/principal-tenures")
public class PrincipalTenureController {

    private final PrincipalTenureService principalTenureService;

    public PrincipalTenureController(PrincipalTenureService principalTenureService) {
        this.principalTenureService = principalTenureService;
    }

    @GetMapping
    public ResponseEntity<List<PrincipalTenure>> getAll() {
        return ResponseEntity.ok(principalTenureService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrincipalTenure> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(principalTenureService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PrincipalTenure> create(@RequestBody PrincipalTenure entity) {
        return ResponseEntity.ok(principalTenureService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrincipalTenure> update(@PathVariable UUID id, @RequestBody PrincipalTenure entity) {
        return ResponseEntity.ok(principalTenureService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        principalTenureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
