package integration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.srd.ediary.EdiaryApplication;
import org.srd.ediary.application.security.dto.TokenRequest;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.OwnerRepository;
import org.testcontainers.shaded.com.trilead.ssh2.auth.AuthenticationManager;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.OwnerTestMother.getOwner;
import static utils.OwnerTestMother.getOwnerBuilder;

@Epic("Integration Tests")
@Feature("Security")
@SpringBootTest(classes = EdiaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("integration_test")
public class TokenGenerationTest {
    @Autowired

    private MockMvc mockMvc;
    @MockBean
    private OwnerRepository ownerRepo;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private JacksonTester<TokenRequest> requestJson;

    private final String login = "example123";
    private final String password = "pass123";
    private final Owner owner = getOwnerBuilder()
            .withLogin(login)
            .withPassword(passwordEncoder.encode(password))
            .build();
    private final TokenRequest tokenRequest = new TokenRequest(login, password);

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void testCreateToken_ValidUser() throws Exception {
        when(ownerRepo.getByLogin(login)).thenReturn(Optional.of(owner));

        mockMvc.perform(post("/api/v1/token/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                .content(requestJson.write(tokenRequest).getJson())
        )
                .andExpect(status().isOk());
    }

    @Test
    void testCreateToken_InvalidUser() throws Exception {
        when(ownerRepo.getByLogin(login)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/token/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson.write(tokenRequest).getJson())
                )
                .andExpect(status().isUnauthorized());
    }
}
