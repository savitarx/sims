package com.invisos.sims.exam.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarksSheetEntryRequestDto {

    @NotNull(message = "Enrollment is required")
    private UUID enrollmentId;

    /** Null clears a previously entered mark. */
    @DecimalMin(value = "0.0", message = "Marks obtained cannot be negative")
    private BigDecimal marksObtained;
}
