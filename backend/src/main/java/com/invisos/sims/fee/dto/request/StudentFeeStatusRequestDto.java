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

    /**
     * Teacher changing the status. Exactly one of {@code updatedById} or
     * {@code updatedByAdminId} must be supplied.
     * TODO: drop once authentication lands and derive from the JWT principal.
     */
    private UUID updatedById;

    /** Admin staff changing the status; the admin-side counterpart of updatedById. */
    private UUID updatedByAdminId;
}
