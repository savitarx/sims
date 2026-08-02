package com.invisos.sims.communication.dto.response;

import com.invisos.sims.common.enums.AnnouncementPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementResponseDto {

    private UUID announcementId;

    private String title;

    private String message;

    private AnnouncementPriority priority;

    private UUID createdById;

    private Instant createdAt;

    private Instant updatedAt;
}
