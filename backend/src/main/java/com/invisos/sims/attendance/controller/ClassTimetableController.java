package com.invisos.sims.attendance.controller;

import com.invisos.sims.attendance.dto.ClassTimeTableBulkRequestDto;
import com.invisos.sims.attendance.dto.ClassTimeTableRequestDto;
import com.invisos.sims.attendance.dto.ClassTimeTableResponseDto;
import com.invisos.sims.attendance.service.ClassTimetableService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/class-timetables")
public class ClassTimetableController {

    private final ClassTimetableService classTimetableService;

    public ClassTimetableController(ClassTimetableService classTimetableService) {
        this.classTimetableService = classTimetableService;
    }

//    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping
    public ResponseEntity<List<ClassTimeTableResponseDto>> getAll(@RequestParam(required = false) UUID sectionId,@RequestParam(required = false) UUID classId, @RequestParam UUID academicYearId) {
        return ResponseEntity.status(HttpStatus.OK).body(classTimetableService.findAll(sectionId,classId,academicYearId));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @GetMapping("/{id}")
    public ResponseEntity<ClassTimeTableResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(classTimetableService.findById(id));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PostMapping
    public ResponseEntity<ClassTimeTableResponseDto> create(@RequestBody ClassTimeTableRequestDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(classTimetableService.create(entity));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @PutMapping("/{id}")
    public ResponseEntity<ClassTimeTableResponseDto> update(@PathVariable UUID id, @RequestBody ClassTimeTableRequestDto entity) {
        return ResponseEntity.ok(classTimetableService.update(id, entity));
    }

//    @PreAuthorize("isAuthenticated()") // TODO: confirm role for this endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        classTimetableService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/bulk")
    public ResponseEntity<List<ClassTimeTableResponseDto>> saveBulk(
            @RequestBody  List< @Valid ClassTimeTableBulkRequestDto> requests) {

        return ResponseEntity.ok(
                classTimetableService.saveBulk(requests)
        );
    }
}
