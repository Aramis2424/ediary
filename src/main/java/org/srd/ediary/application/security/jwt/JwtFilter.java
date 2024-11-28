package org.srd.ediary.application.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.srd.ediary.application.security.OwnerDetails;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int START_INDEX = 7;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        try {
            final String header = request.getHeader(AUTHORIZATION);
            String token = null;
            String username = null;

            if (header != null && header.startsWith(BEARER_PREFIX)) {
                token = header.substring(START_INDEX);
                username = jwtUtils.extractUsername(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                OwnerDetails ownerDetails = new OwnerDetails(username, null, null,
                        jwtUtils.extractUserId(token));

                if (jwtUtils.validateToken(token)) {
                    var authToken = new UsernamePasswordAuthenticationToken(
                            ownerDetails, null, ownerDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException | BadCredentialsException | UnsupportedJwtException |
                 MalformedJwtException | SignatureException jwtException) {
            request.setAttribute("exception", jwtException);
        } catch (Exception ex) {
            request.setAttribute("exception", "Unknown error: " + ex.getMessage());
        }
        chain.doFilter(request, response);
    }
}
