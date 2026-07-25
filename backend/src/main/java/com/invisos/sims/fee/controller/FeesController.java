package com.invisos.sims.fee.controller;

import com.invisos.sims.fee.model.Fees;
import com.invisos.sims.fee.service.FeesService;
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
@RequestMapping("/api/v1/fees")
public class FeesController {

    private final FeesService feesService;

    public FeesController(FeesService feesService) {
        this.feesService = feesService;
    }

    @GetMapping
    public ResponseEntity<List<Fees>> getAll() {
        return ResponseEntity.ok(feesService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fees> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(feesService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Fees> create(@RequestBody Fees entity) {
        return ResponseEntity.ok(feesService.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fees> update(@PathVariable UUID id, @RequestBody Fees entity) {
        return ResponseEntity.ok(feesService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        feesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
