package com.invisos.sims.fee.dto.response;

import com.invisos.sims.common.dto.summary.SectionSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Fee collection screen: one fee, one section, and every student's payment status
 * in a single call.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeStatusSheetResponseDto {

    private FeeSummaryDto fee;

    private SectionSummaryDto section;

    private int totalStudents;

    private long paidCount;

    private long partialCount;

    private long notPaidCount;

    /** Students with no status record yet. */
    private long unrecordedCount;

    private List<FeeStatusSheetEntryDto> entries;
}
