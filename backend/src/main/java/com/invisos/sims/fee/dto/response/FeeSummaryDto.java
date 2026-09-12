package com.invisos.sims.fee.dto.response;

import com.invisos.sims.common.dto.summary.ClassSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/** Compact fee reference embedded in student fee status responses. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeSummaryDto {

    private UUID id;

    private String termName;

    private ClassSummaryDto schoolClass;

    private String academicYearLabel;
}
