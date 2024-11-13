package org.srd.ediary.application.dto;

public record DiaryUpdateDTO(
        Long id,
        Long ownerID,
        String title,
        String description
) {
}
