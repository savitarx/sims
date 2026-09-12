package com.invisos.sims.communication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class EventRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    /** Optional — a null class means the event is school-wide. */
    private UUID classId;

    /**
     * Admin staff creating the event: recorded as createdBy on create and as
     * updatedBy on update. Must be an active ADMIN or PRINCIPAL.
     * TODO: drop once authentication lands and derive from the JWT principal.
     */
    @NotNull(message = "Acting admin id is required")
    private UUID actorId;
}
