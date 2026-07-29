package com.invisos.sims.fee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeResponseDto {

    private UUID feeId;

    private UUID classId;

    private UUID academicYearId;

    private String termName;

    private Instant createdAt;

    private Instant updatedAt;
}
