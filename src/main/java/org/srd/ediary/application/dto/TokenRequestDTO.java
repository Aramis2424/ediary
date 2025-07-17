package org.srd.ediary.application.dto;

public record TokenRequestDTO(
        String login,
        String password
) {
}
