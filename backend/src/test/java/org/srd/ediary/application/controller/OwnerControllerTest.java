package org.srd.ediary.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.srd.ediary.application.dto.OwnerCreateDTO;
import org.srd.ediary.application.dto.OwnerInfoDTO;
import org.srd.ediary.application.dto.TokenRequestDTO;
import org.srd.ediary.application.exception.InvalidCredentialsException;
import org.srd.ediary.application.exception.OwnerAlreadyExistException;
import org.srd.ediary.application.security.OwnerDetails;
import org.srd.ediary.application.security.jwt.JwtFilter;
import org.srd.ediary.application.service.OwnerService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.OwnerTestMother.getOwnerCreateDTO;
import static utils.OwnerTestMother.getOwnerInfoDTO;

@WebMvcTest(OwnerController.class)
@ActiveProfiles("unit_test")
class OwnerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private Authentication authentication;
    @MockBean
    private JwtFilter jwtFilter;
    @MockBean
    private OwnerService ownerService;
    private JacksonTester<OwnerCreateDTO> creationJson;
    private JacksonTester<TokenRequestDTO> loginJson;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        JacksonTester.initFields(this, objectMapper);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new OwnerController(ownerService))
                .setCustomArgumentResolvers(putAuthenticationPrincipal)
                .build();
    }

    @Test
    void testCreateOwner_WithNonExistingLogin() throws Exception {
        OwnerCreateDTO input = getOwnerCreateDTO();
        OwnerInfoDTO output = getOwnerInfoDTO();
        when(ownerService.registerOwner(input)).thenReturn(output);

        mockMvc.perform(post("/api/v1/owners/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(creationJson.write(input).getJson())
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ivan"));
    }

    @Test
    void testCreateOwner_WithAlreadyExistingLogin() throws Exception {
        OwnerCreateDTO input = getOwnerCreateDTO();
        when(ownerService.registerOwner(input)).thenThrow(OwnerAlreadyExistException.class);

        mockMvc.perform(post("/api/v1/owners/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                )
                .andExpect(status().isConflict());
    }

    @Test
    void testFetchOwner_ExistingOwner() throws Exception {
        OwnerInfoDTO output = getOwnerInfoDTO();
        when(ownerService.fetchOwner(1L)).thenReturn(output);

        mockMvc.perform(get("/api/v1/owners/me")
                        .principal(authentication)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan"));
    }

    @Test
    void testFetchOwner_NonExistingOwner() throws Exception {
        when(ownerService.fetchOwner(anyLong())).thenThrow(InvalidCredentialsException.class);

        mockMvc.perform(get("/api/v1/owners/me")
                        .principal(authentication)
                )
                .andExpect(status().isUnauthorized());
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
