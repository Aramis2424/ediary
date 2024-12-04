package org.srd.ediary.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.srd.ediary.application.dto.OwnerCreateDTO;
import org.srd.ediary.application.dto.OwnerInfoDTO;
import org.srd.ediary.application.dto.OwnerLoginDTO;
import org.srd.ediary.application.exception.InvalidCredentialsException;
import org.srd.ediary.application.exception.OwnerAlreadyExistException;
import org.srd.ediary.application.security.jwt.JwtFilter;
import org.srd.ediary.application.service.OwnerService;

import java.io.IOException;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtFilter jwtFilter;
    @MockBean
    private OwnerService ownerService;
    private JacksonTester<OwnerCreateDTO> creationJson;
    private JacksonTester<OwnerLoginDTO> loginJson;

    private final LocalDate birthDate = LocalDate.of(2000, 1, 1);
    private final OwnerInfoDTO output = new OwnerInfoDTO(1L, "Ivan",
            birthDate, "ivan01", LocalDate.now());

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        JacksonTester.initFields(this, objectMapper);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new OwnerController(ownerService))
                .build();
    }

    @Test
    void testCreateOwner_WithNonExistingLogin() throws Exception {
        OwnerCreateDTO input = new OwnerCreateDTO("Ivan", birthDate, "ivan01", "navi01");
        when(ownerService.registerOwner(input)).thenReturn(output);

        mockMvc.perform(post("/owner/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(creationJson.write(input).getJson())
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testCreateOwner_WithAlreadyExistingLogin() throws Exception {
        OwnerCreateDTO input = new OwnerCreateDTO("Ivan", birthDate, "ivan01", "navi01");
        when(ownerService.registerOwner(input)).thenThrow(OwnerAlreadyExistException.class);

        mockMvc.perform(post("/owner/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                )
                .andExpect(status().isConflict());
    }

    @Test
    void testLoginOwner_ExistingOwner() throws Exception {
        OwnerLoginDTO input = new OwnerLoginDTO("ivan01", "navi01");
        when(ownerService.loginOwner(input.login(), input.password())).thenReturn(output);

        mockMvc.perform(post("/owner/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(loginJson.write(input).getJson())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testLoginOwner_NonExistingOwner() throws Exception {
        OwnerLoginDTO input = new OwnerLoginDTO("ivan01", "navi01");
        when(ownerService.loginOwner(input.login(), input.password())).thenThrow(InvalidCredentialsException.class);

        mockMvc.perform(post("/owner/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(loginJson.write(input).getJson())
                )
                .andExpect(status().isUnauthorized());
    }
}
