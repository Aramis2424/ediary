package org.srd.ediary.application.dto;

import java.time.LocalDate;

public record EntryCardDTO(
        Long entryId,
        Long diaryId,
        String title,
        int scoreMood,
        int scoreProductivity,
        LocalDate createdDate
) {
}
