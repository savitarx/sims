package com.invisos.sims.fee.controller;

import com.invisos.sims.fee.dto.request.FeeRequestDto;
import com.invisos.sims.fee.dto.response.FeeResponseDto;
import com.invisos.sims.fee.mapper.FeeMapper;
import com.invisos.sims.fee.model.Fees;
import com.invisos.sims.fee.service.FeesService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fees")
public class FeesController {

    private final FeesService feesService;
    private final FeeMapper feeMapper;

    public FeesController(FeesService feesService, FeeMapper feeMapper) {
        this.feesService = feesService;
        this.feeMapper = feeMapper;
    }

    @GetMapping("/test")
    public String test() {
        return "Fee API Working!";
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER
    @GetMapping
    public ResponseEntity<Page<FeeResponseDto>> getAll(
            @RequestParam(required = false) UUID classId,
            @RequestParam(required = false) UUID academicYearId,
            @PageableDefault(size = 20) Pageable pageable) {

        return ResponseEntity.ok(
                feesService.findAll(classId, academicYearId, pageable).map(feeMapper::toResponse));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL/TEACHER
    @GetMapping("/{id}")
    public ResponseEntity<FeeResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(feeMapper.toResponse(feesService.findById(id)));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PostMapping
    public ResponseEntity<FeeResponseDto> create(@Valid @RequestBody FeeRequestDto request) {
        Fees created = feesService.create(request);
        return ResponseEntity.ok(feeMapper.toResponse(created));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @PutMapping("/{id}")
    public ResponseEntity<FeeResponseDto> update(@PathVariable UUID id,
                                                 @Valid @RequestBody FeeRequestDto request) {
        Fees updated = feesService.update(id, request);
        return ResponseEntity.ok(feeMapper.toResponse(updated));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: Restrict to ADMIN/PRINCIPAL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        feesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
