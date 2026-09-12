package com.invisos.sims.exam.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Bulk save for the marks-entry screen. Marks are entered a section at a time,
 * so the whole grid is submitted in one transactional request.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarksSheetSaveRequestDto {

    @NotNull(message = "Exam subject is required")
    private UUID examSubjectId;

    @NotNull(message = "Section is required")
    private UUID sectionId;

    /**
     * Teacher entering the marks.
     * TODO: drop once authentication lands and derive from the JWT principal.
     */
    @NotNull(message = "Entered by (teacher) is required")
    private UUID enteredById;

    @NotEmpty(message = "At least one entry is required")
    @Valid
    private List<MarksSheetEntryRequestDto> entries;
}
