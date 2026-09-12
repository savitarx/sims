package com.invisos.sims.communication.dto.response;

import com.invisos.sims.common.dto.summary.ActorSummaryDto;
import com.invisos.sims.common.dto.summary.ClassSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponseDto {

    private UUID eventId;

    private String title;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    /** Null means the event is school-wide. */
    private ClassSummaryDto schoolClass;

    /** True when the event applies to the whole school. */
    private boolean schoolWide;

    private ActorSummaryDto createdBy;

    private ActorSummaryDto updatedBy;

    private Instant createdAt;

    private Instant updatedAt;

    // --- deprecated flat ids, retained for one release ---

    /** @deprecated use {@link #schoolClass}. */
    @Deprecated
    private UUID classId;

    /** @deprecated use {@link #createdBy}. */
    @Deprecated
    private UUID createdById;
}
