package org.srd.ediary.application.security.dto;

import lombok.Builder;

@Builder
public record TokenResponse(
        String token
) {
}
