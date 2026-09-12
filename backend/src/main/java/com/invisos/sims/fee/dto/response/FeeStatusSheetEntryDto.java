package com.invisos.sims.fee.dto.response;

import com.invisos.sims.common.dto.summary.ActorSummaryDto;
import com.invisos.sims.common.dto.summary.StudentSummaryDto;
import com.invisos.sims.common.enums.FeeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/** One row of the fee collection grid. {@code status} is null when unrecorded. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeStatusSheetEntryDto {

    private StudentSummaryDto student;

    /** Null until a status has been recorded for this student. */
    private UUID studentFeeStatusId;

    private FeeStatus status;

    private ActorSummaryDto updatedBy;

    private Instant updatedAt;
}
