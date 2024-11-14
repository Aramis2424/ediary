package org.srd.ediary.application.dto;

import java.time.LocalDate;

public record EntryInfoDTO(
        Long id,
        String title,
        String content,
        LocalDate createdDate
) {
}
