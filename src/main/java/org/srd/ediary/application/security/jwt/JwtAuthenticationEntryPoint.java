package org.srd.ediary.application.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        Exception exception = (Exception) request.getAttribute("exception"); // TODO вырезать это из кода (в jftFilter тоже есть это)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(getErrorJson(authException.getMessage()));

        log.error("Authentication Exception: {} ", authException.getMessage(), authException);
    }

    private String getErrorJson(String error) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        errorDetails.put("error", "Unauthorized");
        errorDetails.put("message", error);
        try {
            return mapper.writeValueAsString(errorDetails);
        } catch (JsonProcessingException ex) {
            return "{\"error\": \"Unknown authorization error\"}";
        }
    }
}
