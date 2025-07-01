package org.srd.ediary.application.controller;

import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.srd.ediary.application.security.OwnerDetails;
import org.srd.ediary.application.security.jwt.JwtFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppController.class)
class AppControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private Authentication authentication;
    @MockBean
    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AppController())
                .setCustomArgumentResolvers(putAuthenticationPrincipal)
                .build();
    }

    @Test
    void testGetUser() throws Exception {
        mockMvc.perform(get("/api/v1/")
                        .param("id", "123")
                        .param("name", "Ivan")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("User ID: 123, Name: Ivan\n" +
                                "Server id: 1, Server name: ivan01"));
    }

    @Test
    void testSayHello() throws Exception {
        mockMvc.perform(get("/api/v1/hello")
                        .accept(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().string("Привет, это корневая страница"));

    }

    private final HandlerMethodArgumentResolver putAuthenticationPrincipal = new HandlerMethodArgumentResolver() {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterType().isAssignableFrom(OwnerDetails.class);
        }

        @Override
        public Object resolveArgument(@NonNull MethodParameter parameter,
                                      ModelAndViewContainer mavContainer,
                                      @NonNull NativeWebRequest webRequest,
                                      WebDataBinderFactory binderFactory) throws Exception {
            return new OwnerDetails("ivan01",
                    "navi01", null, 1L);
        }
    };
}