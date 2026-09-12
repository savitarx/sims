package com.invisos.sims.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Publish readiness as structured data so the UI can render a checklist instead
 * of guessing why a publish attempt would fail.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublishPreviewResponseDto {

    private UUID examId;

    private String examName;

    /** True when {@code blockers} is empty. */
    private boolean readyToPublish;

    private int totalSubjects;

    private int scheduledSubjects;

    /** Conditions that will cause publish to be rejected. */
    private List<String> blockers;

    /** Conditions worth surfacing that do not prevent publishing. */
    private List<String> warnings;
}
