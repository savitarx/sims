package com.invisos.sims.auth.controller;

import com.invisos.sims.auth.dto.UsersRequestDto;
import com.invisos.sims.auth.model.Users;
import com.invisos.sims.auth.service.UsersService;
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
@RequestMapping("/api/v1/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping
    public ResponseEntity<List<Users>> getAll() {
        return ResponseEntity.ok(usersService.findAll());
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping("/{id}")
    public ResponseEntity<Users> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(usersService.findById(id));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PostMapping
    public ResponseEntity<Users> create(@RequestBody UsersRequestDto entity) {
        return ResponseEntity.ok(usersService.create(entity));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PutMapping("/{id}")
    public ResponseEntity<Users> update(@PathVariable UUID id, @RequestBody Users entity) {
        return ResponseEntity.ok(usersService.update(id, entity));
    }

    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        usersService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
