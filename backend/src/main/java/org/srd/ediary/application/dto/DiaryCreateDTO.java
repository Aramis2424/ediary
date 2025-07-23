package org.srd.ediary.application.dto;

public record DiaryCreateDTO(
        Long ownerID,
        String title,
        String description
) {
}
