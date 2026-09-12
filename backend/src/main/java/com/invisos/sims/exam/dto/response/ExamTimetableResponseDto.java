package com.invisos.sims.exam.dto.response;

import com.invisos.sims.common.dto.summary.ActorSummaryDto;
import com.invisos.sims.common.dto.summary.ClassSummaryDto;
import com.invisos.sims.common.dto.summary.SubjectSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamTimetableResponseDto {

    private UUID examTimetableId;

    private LocalDate examDate;

    private LocalTime examTime;

    private Integer maxMarks;

    private ExamSummaryDto exam;

    private SubjectSummaryDto subject;

    private ClassSummaryDto schoolClass;

    private ActorSummaryDto updatedBy;

    private Instant createdAt;

    private Instant updatedAt;

    // --- deprecated flat ids, retained for one release ---

    /** @deprecated use {@link #subject} and {@link #exam}. */
    @Deprecated
    private UUID examSubjectId;
}
