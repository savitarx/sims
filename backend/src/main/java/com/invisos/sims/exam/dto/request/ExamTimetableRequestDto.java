package com.invisos.sims.exam.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamTimetableRequestDto {

    @NotNull(message = "Exam subject id is required")
    private UUID examSubjectId;

    @NotNull(message = "Exam date is required")
    private LocalDate examDate;

    @NotNull(message = "Exam time is required")
    private LocalTime examTime;

    /**
     * Admin staff performing the action, recorded as updatedBy.
     * TODO: drop once authentication lands and derive from the JWT principal.
     */
    @NotNull(message = "Acting admin id is required")
    private UUID actorId;
}
