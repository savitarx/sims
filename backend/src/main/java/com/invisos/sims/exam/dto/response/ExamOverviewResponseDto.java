package com.invisos.sims.exam.dto.response;

import com.invisos.sims.common.dto.summary.AcademicYearSummaryDto;
import com.invisos.sims.common.dto.summary.ActorSummaryDto;
import com.invisos.sims.common.enums.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/** Exam dashboard: header, scheduling coverage, and marks-entry progress. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamOverviewResponseDto {

    private UUID examId;

    private String examName;

    private ExamStatus status;

    private AcademicYearSummaryDto academicYear;

    private ActorSummaryDto createdBy;

    private int totalSubjects;

    private int scheduledSubjects;

    private boolean readyToPublish;

    private List<ExamSubjectProgressDto> subjects;
}
