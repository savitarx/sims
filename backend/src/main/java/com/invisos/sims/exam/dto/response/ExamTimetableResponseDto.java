package com.invisos.sims.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamTimetableResponseDto {

    private UUID examTimetableId;

    private UUID examSubjectId;

    private LocalDate examDate;

    private LocalTime examTime;

    private Instant createdAt;

    private Instant updatedAt;
}
