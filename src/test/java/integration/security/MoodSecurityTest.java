package integration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import integration.security.context.WithMockOwnerDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.srd.ediary.EdiaryApplication;
import org.srd.ediary.application.dto.MoodCreateDTO;
import org.srd.ediary.application.dto.MoodUpdateDTO;
import org.srd.ediary.application.security.access.MoodAccess;
import org.srd.ediary.domain.model.Mood;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.MoodRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EdiaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("integrationTest")
public class MoodSecurityTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MoodAccess moodAccess;
    @MockBean
    private MoodRepository moodRepo;

    private JacksonTester<MoodCreateDTO> creationJson;
    private JacksonTester<MoodUpdateDTO> updateJson;

    private final LocalDateTime bedtime = LocalDateTime
            .of(2020, 1,1, 22,30);
    private final LocalDateTime wakeUpTime = LocalDateTime
            .of(2020, 1,2, 8,30);
    private final LocalDate birthdate = LocalDate.of(2000, 1, 1);
    private final Owner owner = new Owner("Ivan", birthdate, "ivan01", "abc123");
    private final Mood moodFromRepo = new Mood(owner, 7, 7, bedtime, wakeUpTime);
    private final List<Mood> listMoodFromRepo = List.of(moodFromRepo);
    private final static long validOwnerId = 1L;
    private final static long invalidOwnerId = 2L;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        JacksonTester.initFields(this, objectMapper);
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testGetMood_WithAccess() throws Exception{
        Long moodId = 1L;
        when(moodRepo.getByID(moodId)).thenReturn(Optional.of(moodFromRepo));
        when(moodAccess.isAllowed(moodId, validOwnerId)).thenReturn(true);

        mockMvc.perform(get("/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(moodId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scoreMood").value("7"));

        verify(moodAccess, times(1)).isAllowed(moodId, validOwnerId);
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testGetMood_WithNoAccess() throws Exception{
        Long moodId = 1L;
        when(moodRepo.getByID(moodId)).thenReturn(Optional.of(moodFromRepo));
        when(moodAccess.isAllowed(moodId, invalidOwnerId)).thenReturn(false);

        mockMvc.perform(get("/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(moodId))
                )
                .andExpect(status().isForbidden());

        verify(moodAccess, times(1)).isAllowed(moodId, invalidOwnerId);
    }

    @Test
    @WithAnonymousUser
    void testGetMood_Unauthorized() throws Exception{
        Long moodId = 1L;

        mockMvc.perform(get("/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(moodId))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testGetMoodsByOwner_WithAccess() throws Exception{
        Long ownerId = validOwnerId;
        when(moodRepo.getAllByOwner(ownerId)).thenReturn(listMoodFromRepo);

        mockMvc.perform(get("/moods/owner/" + ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ownerId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].scoreMood").value(7));
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testGetMoodsByOwner_WithNoAccess() throws Exception{
        Long ownerId = invalidOwnerId;
        when(moodRepo.getAllByOwner(ownerId)).thenReturn(listMoodFromRepo);

        mockMvc.perform(get("/moods/owner/" + ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ownerId))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testCreateMood_WithAccess() throws Exception {
        Long ownerId = validOwnerId;
        MoodCreateDTO input = new MoodCreateDTO(ownerId, 7,7, bedtime, wakeUpTime);
        when(moodRepo.save(any(Mood.class))).thenReturn(moodFromRepo);

        mockMvc.perform(post("/moods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scoreMood").value(7));

        verify(moodRepo, times(1)).save(any(Mood.class));
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testCreateMood_WithNoAccess() throws Exception {
        Long ownerId = invalidOwnerId;
        MoodCreateDTO input = new MoodCreateDTO(ownerId, 7,7, bedtime, wakeUpTime);
        when(moodRepo.save(any(Mood.class))).thenReturn(moodFromRepo);

        mockMvc.perform(post("/moods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                )
                .andExpect(status().isForbidden());

        verify(moodRepo, never()).save(any(Mood.class));
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testUpdateMood_WithAccess() throws Exception {
        Long moodId = 1L;
        MoodUpdateDTO input = new MoodUpdateDTO(7,7, bedtime, wakeUpTime);
        when(moodRepo.getByID(moodId)).thenReturn(Optional.of(moodFromRepo));
        when(moodRepo.save(any(Mood.class))).thenReturn(moodFromRepo);
        when(moodAccess.isAllowed(moodId, validOwnerId)).thenReturn(true);

        mockMvc.perform(put("/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scoreMood").value(7));

        verify(moodRepo, times(1)).save(any(Mood.class));
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testUpdateMood_WithNoAccess() throws Exception {
        Long moodId = 1L;
        MoodUpdateDTO input = new MoodUpdateDTO(7,7, bedtime, wakeUpTime);
        when(moodRepo.getByID(moodId)).thenReturn(Optional.of(moodFromRepo));
        when(moodRepo.save(any(Mood.class))).thenReturn(moodFromRepo);
        when(moodAccess.isAllowed(moodId, invalidOwnerId)).thenReturn(false);

        mockMvc.perform(put("/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                )
                .andExpect(status().isForbidden());

        verify(moodRepo, never()).getByID(moodId);
        verify(moodRepo, never()).save(any(Mood.class));
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testDeleteMood_WithAccess() throws Exception {
        Long moodId = 1L;
        doNothing().when(moodRepo).delete(moodId);
        when(moodAccess.isAllowed(moodId, validOwnerId)).thenReturn(true);

        mockMvc.perform(delete("/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(moodId))
                )
                .andExpect(status().isOk());

        verify(moodRepo, times(1)).delete(moodId);
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testDeleteMood_WithNoAccess() throws Exception {
        Long moodId = 1L;
        doNothing().when(moodRepo).delete(moodId);
        when(moodAccess.isAllowed(moodId, invalidOwnerId)).thenReturn(false);

        mockMvc.perform(delete("/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(moodId))
                )
                .andExpect(status().isForbidden());

        verify(moodRepo, never()).delete(moodId);
    }
}
