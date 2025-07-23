package org.srd.ediary.application.dto;

import java.time.LocalDateTime;

public record MoodCreateDTO(
        Long ownerID,
        int scoreMood,
        int scoreProductivity,
        LocalDateTime bedtime,
        LocalDateTime wakeUpTime
) {
}
