package org.srd.ediary.application.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.srd.ediary.application.security.OwnerDetails;
import org.srd.ediary.application.security.dto.TokenRequest;
import org.srd.ediary.application.security.dto.TokenResponse;
import org.srd.ediary.application.security.jwt.JwtUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public TokenResponse generateToken(TokenRequest tokenRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        tokenRequest.login(), tokenRequest.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        OwnerDetails ownerDetails = getOwnerDetailsFromSecurityContext();
        Map<String, Object> claims = setClaims(ownerDetails);

        String token = jwtUtils.generateToken(claims, ownerDetails.getUsername());
        return TokenResponse.builder()
                .token(token)
                .build();
    }

    private OwnerDetails getOwnerDetailsFromSecurityContext() {
        OwnerDetails ownerDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof OwnerDetails) {
                ownerDetails = (OwnerDetails) principal;
            }
        }
        if (ownerDetails == null) {
            throw new AuthorizationServiceException("Authorization error");
        }
        return ownerDetails;
    }

    private Map<String, Object> setClaims(OwnerDetails ownerDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", ownerDetails.getId());
        return claims;
    }
}
