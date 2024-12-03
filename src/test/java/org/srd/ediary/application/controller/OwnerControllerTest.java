package org.srd.ediary.application.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.srd.ediary.application.security.jwt.JwtFilter;
import org.srd.ediary.application.service.OwnerService;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private Authentication authentication;
    @MockBean
    private JwtFilter jwtFilter;
    @MockBean
    OwnerService ownerService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new OwnerController(ownerService))
                //.setCustomArgumentResolvers(putAuthenticationPrincipal)
                .build();
    }

    @Test
    void testCreateOwner_WithNonExistingLogin() {
    }

    @Test
    void testLoginOwner() {
    }
}