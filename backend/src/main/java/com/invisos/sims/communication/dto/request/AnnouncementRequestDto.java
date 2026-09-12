package com.invisos.sims.communication.dto.request;

import com.invisos.sims.common.enums.AnnouncementPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Message is required")
    @Size(max = 255, message = "Message must not exceed 255 characters")
    private String message;

    /** Optional; defaults to NOTICE. */
    private AnnouncementPriority priority;

    /**
     * Admin staff publishing the announcement: recorded as createdBy on create and
     * as updatedBy on update. Must be an active ADMIN or PRINCIPAL.
     * TODO: drop once authentication lands and derive from the JWT principal.
     */
    @NotNull(message = "Acting admin id is required")
    private UUID actorId;
}
