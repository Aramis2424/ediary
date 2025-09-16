package e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.srd.ediary.EdiaryApplication;
import org.srd.ediary.application.dto.EntryCreateDTO;
import org.srd.ediary.application.dto.EntryUpdateDTO;
import org.srd.ediary.application.security.dto.TokenRequest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static utils.EntryTestMother.getEntryUpdateDTO;

@Epic("E2E Tests")
@Feature("Business")
@SpringBootTest(classes = EdiaryApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("e2e_test")
public class EntryE2EHttpTest {
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.5")).withDatabaseName("test_db2").withUsername("test_user1").withPassword("test_password1");
    private static boolean isDataLoaded = false;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Long ownerId = 11L;
    private final Long diaryId = 14L;
    private final Long entryId = 20L;
    private final String login = "ivan01";
    private final String password = "navi01";
    private final TokenRequest tokenRequest = new TokenRequest(login, password);
    private JacksonTester<TokenRequest> requestJson;
    private JacksonTester<EntryCreateDTO> creationJson;
    private JacksonTester<EntryUpdateDTO> updateJson;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String token;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    private String baseUrl;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        postgresContainer.start();
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @PostConstruct
    void loadTestData() throws IOException {
        if (!isDataLoaded) {
            String sql = Files.readString(Path.of("src/test/resources/db/init.sql"));
            jdbcTemplate.execute(sql);
            System.out.println("Test data initialized.");
            isDataLoaded = true;
        }
    }
    
    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        JacksonTester.initFields(this, objectMapper);
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void checkLoadingData() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM owners", Integer.class);
        assertNotNull(count);
        assertTrue(count > 0);
    }

    @BeforeEach
    @Test
    void getToken() throws IOException {
        String url = baseUrl + "/api/v1/token/create";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(requestJson.write(tokenRequest).getJson(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            token = JsonPath.read(responseBody, "$.token");
        } else {
            throw new IllegalStateException("Failed to get token, status: " + response.getStatusCode().value());
        }
    }

    @Test
    void testUpdatingEntry_FullScenario() throws IOException {
        EntryUpdateDTO input = getEntryUpdateDTO();

        HttpHeaders headers = createHeadersWithToken();
        ResponseEntity<String> diariesResponse = restTemplate.exchange(baseUrl + "/api/v1/owners/" + ownerId + "/diaries", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertEquals(200, diariesResponse.getStatusCode().value());
        assertEquals(2, Optional.ofNullable(JsonPath.read(diariesResponse.getBody(), "$.length()")).orElseThrow());

        String diaryId = JsonPath.read(diariesResponse.getBody(), "$[0].id").toString();

        ResponseEntity<String> entriesResponse = restTemplate.exchange(baseUrl + "/api/v1/diaries/" + diaryId + "/entries", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertEquals(200, entriesResponse.getStatusCode().value());
        assertEquals(3, Optional.ofNullable(JsonPath.read(entriesResponse.getBody(), "$.length()")).orElseThrow());

        String entryId = JsonPath.read(entriesResponse.getBody(), "$[0].id").toString();

        HttpEntity<String> putRequest = new HttpEntity<>(updateJson.write(input).getJson(), headers);
        ResponseEntity<String> updateResponse = restTemplate.exchange(baseUrl + "/api/v1/entries/" + entryId, HttpMethod.PUT, putRequest, String.class);

        assertEquals(200, updateResponse.getStatusCode().value());
        assertEquals("Good day 2", JsonPath.read(updateResponse.getBody(), "$.content"));
    }

    private HttpHeaders createHeadersWithToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }
}
