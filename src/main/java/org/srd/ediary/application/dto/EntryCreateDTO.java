package org.srd.ediary.application.dto;

public record EntryCreateDTO(
        Long diaryID,
        String title,
        String content
) {
}
