package com.invisos.sims.exam.dto.response;

import com.invisos.sims.common.dto.summary.ActorSummaryDto;
import com.invisos.sims.common.dto.summary.StudentSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** One row of the marks-entry grid. {@code marksObtained} is null when unentered. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarksSheetEntryDto {

    private StudentSummaryDto student;

    /** Null until a mark has been saved for this student. */
    private UUID markId;

    private BigDecimal marksObtained;

    private ActorSummaryDto enteredBy;

    private Instant updatedAt;
}
