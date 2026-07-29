package com.invisos.sims.fee.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class FeeRequestDto {

    @NotNull(message = "Class id is required")
    private UUID classId;

    @NotNull(message = "Academic year id is required")
    private UUID academicYearId;

    @NotBlank(message = "Term name is required")
    private String termName;
}
