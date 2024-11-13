package org.srd.ediary.application.dto;

import java.time.LocalDate;

public record DiaryInfoDTO(
        String title,
        String description,
        int cntEntry,
        LocalDate createdDate
) {
}
