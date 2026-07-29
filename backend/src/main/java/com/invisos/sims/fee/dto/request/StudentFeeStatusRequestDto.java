package com.invisos.sims.fee.dto.request;

import com.invisos.sims.common.enums.FeeStatus;
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
public class StudentFeeStatusRequestDto {

    @NotNull(message = "Enrollment id is required")
    private UUID enrollmentId;

    @NotNull(message = "Fee id is required")
    private UUID feeId;

    @NotNull(message = "Status is required")
    private FeeStatus status;

    @NotNull(message = "Updated by (teacher id) is required")
    private UUID updatedById;
}
