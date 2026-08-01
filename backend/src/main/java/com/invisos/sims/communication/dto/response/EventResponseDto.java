package com.invisos.sims.communication.dto.response;

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

    // Null means the event is school-wide.
    private UUID classId;

    private UUID createdById;

    private Instant createdAt;

    private Instant updatedAt;
}
