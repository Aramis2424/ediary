package org.srd.ediary.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MoodInfoDTO(
        Long id,
        int scoreMood,
        int scoreProductivity,
        LocalDateTime bedtime,
        LocalDateTime wakeUpTime,
        LocalDate createdAt
) {
}
