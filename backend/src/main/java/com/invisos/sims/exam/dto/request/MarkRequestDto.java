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
public class MarkRequestDto {

    @NotNull(message = "Enrollment is required")
    private UUID enrollmentId;

    @NotNull(message = "Exam subject is required")
    private UUID examSubjectId;

    @NotNull(message = "Marks obtained is required")
    @DecimalMin(value = "0.0", message = "Marks obtained cannot be negative")
    private BigDecimal marksObtained;

    @NotNull(message = "Entered by (teacher) is required")
    private UUID enteredById;
}
