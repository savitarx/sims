package com.invisos.sims.exam.dto.response;

import com.invisos.sims.common.dto.summary.SectionSummaryDto;
import com.invisos.sims.common.dto.summary.SubjectSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Everything the marks-entry screen needs in a single call: the exam header, the
 * subject being marked, the section, and the full student roster with any marks
 * already recorded pre-filled.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarksSheetResponseDto {

    private UUID examSubjectId;

    private ExamSummaryDto exam;

    private SubjectSummaryDto subject;

    private SectionSummaryDto section;

    private Integer maxMarks;

    /** True once every student in the roster has a mark. */
    private boolean complete;

    private int totalStudents;

    private int marksEntered;

    /** Read-only when the exam is published. */
    private boolean editable;

    private List<MarksSheetEntryDto> entries;
}
