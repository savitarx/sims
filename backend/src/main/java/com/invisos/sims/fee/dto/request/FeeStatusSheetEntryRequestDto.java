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
public class FeeStatusSheetEntryRequestDto {

    @NotNull(message = "Enrollment is required")
    private UUID enrollmentId;

    @NotNull(message = "Status is required")
    private FeeStatus status;
}
