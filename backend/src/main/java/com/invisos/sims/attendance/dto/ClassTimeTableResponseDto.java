package com.invisos.sims.attendance.dto;

import com.invisos.sims.common.enums.DayOfWeek;
import com.invisos.sims.common.enums.UserStatus;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassTimeTableResponseDto {

    private UUID timetableId;

    private UUID sectionId;
    private String sectionName;

    private DayOfWeek day;

    private Integer periodNumber;

    private LocalTime startTime;
    private LocalTime endTime;

    private UUID subjectId;
    private String className;
    private String subjectName;

    private UUID teacherId;
    private String teacherName;
    private UserStatus teacherStatus;
}
