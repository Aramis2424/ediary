package org.srd.ediary.application.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {
    private final String jwtSecret;
    private final int jwtExpirationMs;

    public JwtUtils(@Value("${jwt.secret}") String jwtSecret,
                    @Value("${jwt.expiration}") int jwtExpirationMs) {
        this.jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String bearerToken) {
        return extractClaimBody(bearerToken, Claims::getSubject);
    }

    public Long extractUserId(String bearerToken) {
        return extractClaimBody(bearerToken, getUserId());
    }

    public <T> T extractClaimBody(String bearerToken, Function<Claims, T> claimsResolver) {
        Jws<Claims> jwsClaims = extractClaims(bearerToken);
        return claimsResolver.apply(jwsClaims.getBody());
    }

    private Jws<Claims> extractClaims(String bearerToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(bearerToken);
    }

    private Function<Claims, Long> getUserId() {
        return claims -> claims.get("userId", Long.class);
    }

    public boolean validateToken(String token) {
        final String userName = extractUsername(token);
        return isTokenAlive(token);
    }

    private Boolean isTokenAlive(String bearerToken) {
        return extractExpiry(bearerToken).after(new Date());
    }

    public Date extractExpiry(String bearerToken) {
        return extractClaimBody(bearerToken, Claims::getExpiration);
    }
}
