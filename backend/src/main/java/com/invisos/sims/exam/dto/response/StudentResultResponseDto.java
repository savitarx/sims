package com.invisos.sims.exam.dto.response;

import com.invisos.sims.common.dto.summary.SectionSummaryDto;
import com.invisos.sims.common.dto.summary.StudentSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/** A single student's result across every subject of one exam. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResultResponseDto {

    private ExamSummaryDto exam;

    private StudentSummaryDto student;

    private SectionSummaryDto section;

    private List<StudentResultSubjectDto> subjects;

    private BigDecimal totalMarksObtained;

    private int totalMaxMarks;

    /** Percentage across all marked subjects, 2 dp. Null when nothing is marked. */
    private BigDecimal percentage;

    private int subjectsMarked;

    private int subjectsTotal;
}
