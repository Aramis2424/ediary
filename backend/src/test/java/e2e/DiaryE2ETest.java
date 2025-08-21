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
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.srd.ediary.EdiaryApplication;
import org.srd.ediary.application.dto.DiaryCreateDTO;
import org.srd.ediary.application.dto.DiaryUpdateDTO;
import org.srd.ediary.application.security.dto.TokenRequest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Epic("E2E Tests")
@Feature("Business")
@SpringBootTest(classes = EdiaryApplication.class)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("e2e_test")
public class DiaryE2ETest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static boolean isDataLoaded = false;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private JacksonTester<TokenRequest> requestJson;
    private JacksonTester<DiaryCreateDTO> creationJson;
    private JacksonTester<DiaryUpdateDTO> updateJson;

    private final Long ownerId = 11L;
    private final Long diaryId = 14L;
    private final String login = "ivan01";
    private final String password = "navi01";
    private final TokenRequest tokenRequest = new TokenRequest(login, password);
    private String token;

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:14.5"))
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_password");

    @PostConstruct
    void loadTestData() throws IOException {
        if (!isDataLoaded) {
            String sql = Files.readString(Path.of("src/test/resources/db/init.sql"));
            jdbcTemplate.execute(sql);
            System.out.println("Test data initialized.");
            isDataLoaded = true;
        }
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        postgresContainer.start();
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void checkLoadingData() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM owners", Integer.class);
        assertNotNull(count);
        assertTrue(count > 0);
    }

    @BeforeEach
    @Test
    void getToken() throws Exception{
        MvcResult result = mockMvc.perform(post("/api/v1/token/create")
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
    void getToken_Invalid() throws Exception{
        TokenRequest invalidTokenRequest = new TokenRequest("err", "err");
        mockMvc.perform(post("/api/v1/token/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson.write(invalidTokenRequest).getJson())
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetOwner() throws Exception {
        mockMvc.perform(get("/api/v1")
                        .param("id", "-1")
                        .param("name", "Ivan")
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString("ivan01"))
                );
    }

    @Test
    void testGetDiaryById_Valid() throws Exception {
        mockMvc.perform(get("/api/v1/diaries/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Direction."));
    }

    @Test
    void testGetDiaryById_InvalidToken() throws Exception {
        mockMvc.perform(get("/api/v1/diaries/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                        .header("Authorization", "Bearer " + "abc")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetDiaryById_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/diaries/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetDiaryById_Forbidden() throws Exception {
        Long alienDiaryId = 13L;

        mockMvc.perform(get("/api/v1/diaries/" + alienDiaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(alienDiaryId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetDiariesByOwner_WithAccess() throws Exception{
        mockMvc.perform(get("/api/v1/owners/" + ownerId + "/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ownerId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetDiariesByOwner_WithNoAccess() throws Exception{
        Long alienOwnerId = 1L;
        mockMvc.perform(get("/api/v1/owners/" + alienOwnerId + "/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(alienOwnerId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateDiary_WithAccess() throws Exception {
        DiaryCreateDTO input = new DiaryCreateDTO(ownerId, "d1", "of1");

        mockMvc.perform(post("/api/v1/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("d1"))
                .andExpect(jsonPath("$.description").value("of1"));
    }

    @Test
    void testCreateDiary_WithNoAccess() throws Exception {
        Long alienOwnerId = 1L;
        DiaryCreateDTO input = new DiaryCreateDTO(alienOwnerId, "d1", "of1");

        mockMvc.perform(post("/api/v1/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateDiary_WithAccess() throws Exception {
        DiaryUpdateDTO input = new DiaryUpdateDTO("Direction.", "of1");

        mockMvc.perform(put("/api/v1/diaries/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Direction."))
                .andExpect(jsonPath("$.description").value("of1"));
    }

    @Test
    void testUpdateDiary_WithNoAccess() throws Exception {
        long alienDiaryId = 1L;
        DiaryUpdateDTO input = new DiaryUpdateDTO("d1", "of1");

        mockMvc.perform(put("/api/v1/diaries/" + alienDiaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteDiary_WithNoAccess() throws Exception {
        Long alienDiaryId = 1L;
        mockMvc.perform(delete("/api/v1/diaries/" + alienDiaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(alienDiaryId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }
}
