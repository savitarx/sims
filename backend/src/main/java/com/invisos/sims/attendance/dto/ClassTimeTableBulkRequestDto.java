package com.invisos.sims.attendance.dto;

import com.invisos.sims.common.enums.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassTimeTableBulkRequestDto {

    private UUID timetableId;

    @NotNull
    private UUID sectionId;

    @NotNull
    private DayOfWeek day;

    @NotNull
    private Integer periodNumber;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private UUID subjectId;

    @NotNull
    private UUID teacherId;
}