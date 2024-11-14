package org.srd.ediary.application.dto;

import java.time.LocalDate;

public record EntryCreateDTO(
        Long diaryID,
        String title,
        String content
) {
}
