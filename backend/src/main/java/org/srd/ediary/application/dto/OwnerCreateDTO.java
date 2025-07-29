package org.srd.ediary.application.dto;

import java.time.LocalDate;

public record OwnerCreateDTO(
        String name,
        LocalDate birthDate,
        String login,
        String password) {
}
