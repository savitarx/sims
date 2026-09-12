package com.invisos.sims.fee.dto.response;

import com.invisos.sims.common.dto.summary.AcademicYearSummaryDto;
import com.invisos.sims.common.dto.summary.ActorSummaryDto;
import com.invisos.sims.common.dto.summary.ClassSummaryDto;
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
public class FeeResponseDto {

    private UUID feeId;

    private String termName;

    private ClassSummaryDto schoolClass;

    private AcademicYearSummaryDto academicYear;

    private ActorSummaryDto updatedBy;

    private Instant createdAt;

    private Instant updatedAt;

    // --- deprecated flat ids, retained for one release ---

    /** @deprecated use {@link #schoolClass}. */
    @Deprecated
    private UUID classId;

    /** @deprecated use {@link #academicYear}. */
    @Deprecated
    private UUID academicYearId;
}
