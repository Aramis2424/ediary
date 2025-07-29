package org.srd.ediary.application.dto;

import java.time.LocalDate;

public record OwnerInfoDTO(
        Long id,
        String name,
        LocalDate birthDate,
        String login,
        LocalDate createdDate) {
}
