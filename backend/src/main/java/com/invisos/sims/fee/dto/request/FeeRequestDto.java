package com.invisos.sims.fee.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class FeeRequestDto {

    @NotNull(message = "Class id is required")
    private UUID classId;

    @NotNull(message = "Academic year id is required")
    private UUID academicYearId;

    @NotBlank(message = "Term name is required")
    @Size(max = 255, message = "Term name must not exceed 255 characters")
    private String termName;

    /**
     * Admin staff performing the action, recorded as updatedBy.
     * TODO: drop once authentication lands and derive from the JWT principal.
     */
    @NotNull(message = "Acting admin id is required")
    private UUID actorId;
}
