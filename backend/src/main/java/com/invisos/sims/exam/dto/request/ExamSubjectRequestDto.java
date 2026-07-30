package com.invisos.sims.exam.dto.request;

import jakarta.validation.constraints.Min;
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
public class ExamSubjectRequestDto {

    @NotNull(message = "Exam is required")
    private UUID examId;

    @NotNull(message = "Subject is required")
    private UUID subjectId;

    @NotNull(message = "Class is required")
    private UUID classId;

    @NotNull(message = "Max marks is required")
    @Min(value = 1, message = "Max marks must be at least 1")
    private Integer maxMarks;
}
