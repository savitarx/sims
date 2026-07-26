package com.invisos.sims.exam.dto.response;

import com.invisos.sims.common.enums.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResponseDto {

    private UUID examId;

    private UUID academicYearId;

    private String examName;

    private ExamStatus status;

    private UUID createdById;

    private Instant createdAt;

    private Instant updatedAt;
}