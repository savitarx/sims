package com.invisos.sims.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkResponseDto {

    private UUID markId;

    private UUID enrollmentId;

    private UUID examSubjectId;

    private BigDecimal marksObtained;

    private UUID enteredById;

    private Instant createdAt;

    private Instant updatedAt;
}
