package e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import integration.security.context.WithMockOwnerDetails;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.srd.ediary.EdiaryApplication;
import org.srd.ediary.application.dto.EntryCreateDTO;
import org.srd.ediary.application.dto.EntryUpdateDTO;
import org.srd.ediary.application.security.dto.TokenRequest;
import org.srd.ediary.domain.model.Entry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EdiaryApplication.class)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("e2e_test")
public class EntryE2ETest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static boolean isDataLoaded = false;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private JacksonTester<TokenRequest> requestJson;
    private JacksonTester<EntryCreateDTO> creationJson;
    private JacksonTester<EntryUpdateDTO> updateJson;

    private final Long ownerId = 11L;
    private final Long diaryId = 14L;
    private final Long entryId = 20L;
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
    void testGetEntry_WithAccess() throws Exception{
        mockMvc.perform(get("/entries/" + entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(entryId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Mr lawyer."));
    }

    @Test
    void testGetEntry_WithNoAccess() throws Exception{
        Long alienEntryId = 1L;

        mockMvc.perform(get("/entries/" + alienEntryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(alienEntryId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetEntry_Unauthorized() throws Exception{
        mockMvc.perform(get("/entries/" + entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(entryId))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetEntriesByDiary_WithAccess() throws Exception{
        mockMvc.perform(get("/entries/diary/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void testGetEntriesByDiary_WithNoAccess() throws Exception{
        Long alienDiaryId = 1L;

        mockMvc.perform(get("/entries/diary/" + alienDiaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(alienDiaryId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());}

    @Test
    @Transactional
    void testCreateEntry_WithAccess() throws Exception {
        EntryCreateDTO input = new EntryCreateDTO(diaryId, "day1", "good1");

        mockMvc.perform(post("/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("day1"))
                .andExpect(jsonPath("$.content").value("good1"));
    }

    @Test
    void testCreateEntry_WithNoAccess() throws Exception {
        Long alienDiaryId = 1L;
        EntryCreateDTO input = new EntryCreateDTO(alienDiaryId, "day1", "good1");
        mockMvc.perform(post("/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateEntry_WithAccess() throws Exception {
        EntryUpdateDTO input = new EntryUpdateDTO("Mr lawyer.", "good1");

        mockMvc.perform(put("/entries/" + entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("good1"));
    }

    @Test
    void testUpdateEntry_WithNoAccess() throws Exception {
        long alienEntryId = 1L;
        EntryUpdateDTO input = new EntryUpdateDTO("day1", "good1");

        mockMvc.perform(put("/entries/" + alienEntryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void testDeleteEntry_WithAccess() throws Exception {
        mockMvc.perform(delete("/entries/" + entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(entryId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteEntry_WithNoAccess() throws Exception {
        Long alienEntryId = 1L;

        mockMvc.perform(delete("/entries/" + alienEntryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(alienEntryId))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isForbidden());
    }
}
