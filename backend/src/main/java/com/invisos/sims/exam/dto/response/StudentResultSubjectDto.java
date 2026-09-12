package com.invisos.sims.exam.dto.response;

import com.invisos.sims.common.dto.summary.SubjectSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResultSubjectDto {

    private UUID examSubjectId;

    private SubjectSummaryDto subject;

    private Integer maxMarks;

    /** Null when the subject has not been marked for this student yet. */
    private BigDecimal marksObtained;

    private BigDecimal percentage;
}
