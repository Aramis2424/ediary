package org.srd.ediary.application.dto;

import java.time.LocalDate;

public record EntryUpdateDTO(
        String title,
        String content
) {
}
