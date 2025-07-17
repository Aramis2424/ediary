package org.srd.ediary.application.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.srd.ediary.application.security.dto.TokenRequest;
import org.srd.ediary.application.security.dto.TokenResponse;
import org.srd.ediary.application.security.service.TokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "owner", description = "Operations about user")
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/token/create")
    @Operation(summary = "Login user")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest tokenRequest) {
        return new ResponseEntity<>(tokenService.generateToken(tokenRequest), HttpStatus.OK);
    }
}
