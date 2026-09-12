package com.invisos.sims.exam.dto.response;

import com.invisos.sims.common.enums.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/** Compact exam reference embedded in exam-subject, timetable and mark responses. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSummaryDto {

    private UUID id;

    private String name;

    private ExamStatus status;
}
