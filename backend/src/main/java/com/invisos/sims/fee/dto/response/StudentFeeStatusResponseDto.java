package com.invisos.sims.fee.dto.response;

import com.invisos.sims.common.dto.summary.ActorSummaryDto;
import com.invisos.sims.common.dto.summary.SectionSummaryDto;
import com.invisos.sims.common.dto.summary.StudentSummaryDto;
import com.invisos.sims.common.enums.FeeStatus;
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
public class StudentFeeStatusResponseDto {

    private UUID studentFeeStatusId;

    private FeeStatus status;

    private StudentSummaryDto student;

    private SectionSummaryDto section;

    private FeeSummaryDto fee;

    /** Whoever last changed the status — a teacher or an admin. */
    private ActorSummaryDto updatedBy;

    private Instant createdAt;

    private Instant updatedAt;

    // --- deprecated flat ids, retained for one release ---

    /** @deprecated use {@link #student}. */
    @Deprecated
    private UUID enrollmentId;

    /** @deprecated use {@link #fee}. */
    @Deprecated
    private UUID feeId;

    /** @deprecated use {@link #updatedBy}. */
    @Deprecated
    private UUID updatedById;
}
