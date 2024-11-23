package org.srd.ediary.application.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.srd.ediary.application.security.AuthUserDetailsService;
import org.srd.ediary.application.security.dto.TokenRequest;
import org.srd.ediary.application.security.dto.TokenResponse;
import org.srd.ediary.application.security.jwt.JwtUtils;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final AuthenticationManager authenticationManager;
    private final AuthUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    public TokenResponse generateToken(TokenRequest tokenRequest) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        tokenRequest.username(), tokenRequest.password())
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(tokenRequest.username());
        String token = jwtUtils.generateToken(Collections.emptyMap(), userDetails.getUsername());
        return TokenResponse.builder()
                .token(token)
                .build();
    }
}
