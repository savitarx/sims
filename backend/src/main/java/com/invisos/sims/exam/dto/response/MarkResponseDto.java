package com.invisos.sims.exam.dto.response;

import com.invisos.sims.common.dto.summary.ActorSummaryDto;
import com.invisos.sims.common.dto.summary.StudentSummaryDto;
import com.invisos.sims.common.dto.summary.SubjectSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkResponseDto {

    private UUID markId;

    private BigDecimal marksObtained;

    private Integer maxMarks;

    private StudentSummaryDto student;

    private SubjectSummaryDto subject;

    private ExamSummaryDto exam;

    private ActorSummaryDto enteredBy;

    private ActorSummaryDto updatedBy;

    private Instant createdAt;

    private Instant updatedAt;

    // --- deprecated flat ids, retained for one release ---

    /** @deprecated use {@link #student}. */
    @Deprecated
    private UUID enrollmentId;

    /** @deprecated use {@link #subject} and {@link #maxMarks}. */
    @Deprecated
    private UUID examSubjectId;

    /** @deprecated use {@link #enteredBy}. */
    @Deprecated
    private UUID enteredById;
}
