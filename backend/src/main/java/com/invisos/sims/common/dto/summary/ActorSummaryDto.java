package com.invisos.sims.common.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * A person who acted on a record (created it, updated it, entered marks).
 * Resolved from either {@code AdminStaff} or {@code Teachers}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActorSummaryDto {

    private UUID id;

    private String name;

    /** Designation of the actor, e.g. PRINCIPAL, SENIOR_TEACHER. */
    private String role;
}
