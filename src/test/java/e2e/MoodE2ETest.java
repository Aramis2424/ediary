package e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
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
import org.springframework.transaction.annotation.Transactional;
import org.srd.ediary.EdiaryApplication;
import org.srd.ediary.application.dto.MoodCreateDTO;
import org.srd.ediary.application.dto.MoodUpdateDTO;
import org.srd.ediary.application.security.dto.TokenRequest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EdiaryApplication.class)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("e2e_test")
public class MoodE2ETest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static boolean isDataLoaded = false;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private JacksonTester<TokenRequest> requestJson;
    private JacksonTester<MoodCreateDTO> creationJson;
    private JacksonTester<MoodUpdateDTO> updateJson;

    private final LocalDateTime bedtime = LocalDateTime
            .of(2020, 1,1, 22,30);
    private final LocalDateTime wakeUpTime = LocalDateTime
            .of(2020, 1,2, 8,30);
    private final Long ownerId = 11L;
    private final Long moodId = 23L;
    private final String login = "ivan01";
    private final String password = "navi01";
    private final TokenRequest tokenRequest = new TokenRequest(login, password);
    private String token;

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:14.5"))
            .withDatabaseName("test_db1")
            .withUsername("test_user1")
            .withPassword("test_password1");

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
    void testGetMood_WithAccess() throws Exception{
        mockMvc.perform(get("/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(moodId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scoreMood").value("4"));
    }

    @Test
    void testGetMood_WithNoAccess() throws Exception{
        Long alienMoodId = 1L;

        mockMvc.perform(get("/moods/" + alienMoodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(alienMoodId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetMoodsByOwner_WithAccess() throws Exception{
        mockMvc.perform(get("/moods/owner/" + ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ownerId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void testGetMoodsByOwner_WithNoAccess() throws Exception{
        Long alienOwnerId = 1L;

        mockMvc.perform(get("/moods/owner/" + alienOwnerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(alienOwnerId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void testCreateMood_WithAccess() throws Exception {
        MoodCreateDTO input = new MoodCreateDTO(ownerId, 7,7, bedtime, wakeUpTime);

        mockMvc.perform(post("/moods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scoreMood").value(7));
    }

    @Test
    void testCreateMood_WithNoAccess() throws Exception {
        Long alienOwnerId = 1L;
        MoodCreateDTO input = new MoodCreateDTO(alienOwnerId, 7,7, bedtime, wakeUpTime);

        mockMvc.perform(post("/moods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateMood_WithAccess() throws Exception {
        MoodUpdateDTO input = new MoodUpdateDTO(4,7, bedtime, wakeUpTime);

        mockMvc.perform(put("/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scoreMood").value(4));
    }

    @Test
    void testUpdateMood_WithNoAccess() throws Exception {
        long alienMoodId = 1L;
        MoodUpdateDTO input = new MoodUpdateDTO(4,7, bedtime, wakeUpTime);

        mockMvc.perform(put("/moods/" + alienMoodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteMood_WithNoAccess() throws Exception {
        Long alienMoodId = 1L;

        mockMvc.perform(delete("/moods/" + alienMoodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(alienMoodId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }
}
