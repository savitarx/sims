package com.invisos.sims.attendance.dto;

import com.invisos.sims.common.enums.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassTimeTableRequestDto {

    @NotNull(message = "Section ID is required.")
    private UUID sectionId;

    @NotNull(message = "Day is required.")
    private DayOfWeek day;

    @NotNull(message = "Period number is required.")
    @Positive(message = "Period number must be greater than 0.")
    private Integer periodNumber;

    @NotNull(message = "Start time is required.")
    private LocalTime startTime;

    @NotNull(message = "End time is required.")
    private LocalTime endTime;

    @NotNull(message = "Subject ID is required.")
    private UUID subjectId;

    @NotNull(message = "Teacher ID is required.")
    private UUID teacherId;
}
