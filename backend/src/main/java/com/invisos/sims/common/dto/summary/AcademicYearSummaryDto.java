package com.invisos.sims.common.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYearSummaryDto {

    private UUID id;

    private String label;

    private LocalDate startDate;

    private LocalDate endDate;
}
