package org.srd.ediary.application.dto;

import java.time.LocalDateTime;

public record MoodUpdateDTO(
        int scoreMood,
        int scoreProductivity,
        LocalDateTime bedtime,
        LocalDateTime wakeUpTime
) {
}
