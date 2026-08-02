package com.invisos.sims.fee.dto.response;

import com.invisos.sims.common.enums.FeeStatus;
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
public class StudentFeeStatusResponseDto {

    private UUID studentFeeStatusId;

    private UUID enrollmentId;

    private UUID feeId;

    private FeeStatus status;

    private UUID updatedById;

    private Instant createdAt;

    private Instant updatedAt;
}
