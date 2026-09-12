package com.invisos.sims.exam.dto.response;

import com.invisos.sims.common.dto.summary.AcademicYearSummaryDto;
import com.invisos.sims.common.dto.summary.ActorSummaryDto;
import com.invisos.sims.common.enums.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResponseDto {

    private UUID examId;

    private String examName;

    private ExamStatus status;

    private AcademicYearSummaryDto academicYear;

    private ActorSummaryDto createdBy;

    private ActorSummaryDto updatedBy;

    private Instant createdAt;

    private Instant updatedAt;

    // --- deprecated flat ids, retained for one release ---

    /** @deprecated use {@link #academicYear}. */
    @Deprecated
    private UUID academicYearId;

    /** @deprecated use {@link #createdBy}. */
    @Deprecated
    private UUID createdById;
}
