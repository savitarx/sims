package com.invisos.sims.exam.dto.response;

import com.invisos.sims.common.dto.summary.ClassSummaryDto;
import com.invisos.sims.common.dto.summary.SubjectSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/** One subject's readiness within an exam: scheduled yet, and how many marks are in. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSubjectProgressDto {

    private UUID examSubjectId;

    private SubjectSummaryDto subject;

    private ClassSummaryDto schoolClass;

    private Integer maxMarks;

    private boolean scheduled;

    private LocalDate examDate;

    private LocalTime examTime;

    private long marksEntered;
}
