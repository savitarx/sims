package com.invisos.sims.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeachersAssignmentResponseDto {

    private UUID assignmentId;

    private UUID teacherId;
    private String teacherName;

    private UUID academicYearId;
    private String academicYearLabel;

    private UUID sectionId;
    private String sectionName;

    private UUID subjectId;
    private String subjectName;

    private UUID assignedById;
    private String assignedByName;


}