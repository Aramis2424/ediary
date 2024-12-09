package e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.srd.ediary.EdiaryApplication;
import org.srd.ediary.application.security.dto.TokenRequest;
import org.srd.ediary.domain.model.Owner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EdiaryApplication.class)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("e2e_test")
@Sql(scripts = "/db/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class DiaryE2ETest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private JacksonTester<TokenRequest> requestJson;

    private final String login = "ivan01";
    private final String password = "navi01";
    private final LocalDate birthDate = LocalDate.of(2000, 1, 1);
    private final TokenRequest tokenRequest = new TokenRequest(login, password);
    private String token;

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:14.5"))
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_password");

    @BeforeAll
    static void setUpContainer() {
        postgresContainer.start();
        System.setProperty("DB_URL", postgresContainer.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgresContainer.getUsername());
        System.setProperty("DB_PASSWORD", postgresContainer.getPassword());
    }

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        JacksonTester.initFields(this, objectMapper);
    }

    @BeforeEach
    @Test
    void checkLoadingData() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM owners", Integer.class);
        System.out.println("Number of owners: " + count);
        assertNotNull(count);
        assertTrue(count > 0);
    }

    @BeforeEach
    @Test
    void getToken() throws Exception{
        MvcResult result = mockMvc.perform(post("/token/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson.write(tokenRequest).getJson())
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        token = JsonPath.read(responseBody, "$.token");
    }

    @Test
    void testGetDiaryById() throws Exception {
        mockMvc.perform(get("/")
                        .param("id", "123")
                        .param("name", "Ivan")
                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString("Ivan"))
                );
    }
}
