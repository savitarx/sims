package com.invisos.sims.fee.dto.request;

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
 * Bulk update for the fee collection screen: a whole section's statuses for one
 * fee, saved in a single transaction.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeStatusSheetSaveRequestDto {

    @NotNull(message = "Fee id is required")
    private UUID feeId;

    @NotNull(message = "Section id is required")
    private UUID sectionId;

    /**
     * Teacher recording the statuses. Exactly one of {@code updatedById} or
     * {@code updatedByAdminId} must be supplied.
     * TODO: drop once authentication lands and derive from the JWT principal.
     */
    private UUID updatedById;

    /** Admin staff recording the statuses. */
    private UUID updatedByAdminId;

    @NotEmpty(message = "At least one entry is required")
    @Valid
    private List<FeeStatusSheetEntryRequestDto> entries;
}
