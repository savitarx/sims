package com.invisos.sims.exam.dto.request;

import com.invisos.sims.common.enums.ExamStatus;
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
public class ExamRequestDto {

    @NotNull(message = "Academic Year is required")
    private UUID academicYearId;

    @NotBlank(message = "Exam name is required")
    private String examName;

    @NotNull(message = "Exam status is required")
    private ExamStatus status;
}