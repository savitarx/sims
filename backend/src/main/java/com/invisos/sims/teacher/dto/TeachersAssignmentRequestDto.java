package com.invisos.sims.teacher.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeachersAssignmentRequestDto {

    @NotNull(message = "Teacher ID is required.")
    private UUID teacherId;

    @NotNull(message = "Academic year ID is required.")
    private UUID academicYearId;

    @NotNull(message = "Section ID is required.")
    private UUID sectionId;

    @NotNull(message = "Subject ID is required.")
    private UUID subjectId;

    @NotNull(message = "Assigned-by (admin) ID is required.")
    private UUID assignedById;
}