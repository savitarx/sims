package com.invisos.sims.exam.dto.request;

import com.invisos.sims.common.enums.ExamStatus;
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
public class ExamRequestDto {

    @NotNull(message = "Academic Year is required")
    private UUID academicYearId;

    @NotBlank(message = "Exam name is required")
    @Size(max = 255, message = "Exam name must not exceed 255 characters")
    private String examName;

    /** Optional; defaults to DRAFT. Publishing is done via the publish endpoint. */
    private ExamStatus status;

    /**
     * Admin staff performing the action: recorded as createdBy on create and as
     * updatedBy on update.
     * TODO: drop once authentication lands and derive from the JWT principal.
     */
    @NotNull(message = "Acting admin id is required")
    private UUID actorId;
}
