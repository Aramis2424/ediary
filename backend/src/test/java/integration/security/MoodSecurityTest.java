package integration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import integration.context.WithMockOwnerDetails;
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
import org.srd.ediary.domain.repository.OwnerRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.MoodTestMother.*;
import static utils.OwnerTestMother.getOwner;

@SpringBootTest(classes = EdiaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("integration_test")
public class MoodSecurityTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MoodAccess moodAccess;
    @MockBean
    private MoodRepository moodRepo;
    @MockBean
    private OwnerRepository ownerRepo;

    private JacksonTester<MoodCreateDTO> creationJson;
    private JacksonTester<MoodUpdateDTO> updateJson;

    private final Owner owner = getOwner();
    private final Mood moodFromRepo = getMood1();
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

        mockMvc.perform(get("/api/v1/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(moodId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scoreMood").value("1"));

        verify(moodAccess, times(1)).isAllowed(moodId, validOwnerId);
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testGetMood_WithNoAccess() throws Exception{
        Long moodId = 1L;
        when(moodRepo.getByID(moodId)).thenReturn(Optional.of(moodFromRepo));
        when(moodAccess.isAllowed(moodId, invalidOwnerId)).thenReturn(false);

        mockMvc.perform(get("/api/v1/moods/" + moodId)
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

        mockMvc.perform(get("/api/v1/moods/" + moodId)
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

        mockMvc.perform(get("/api/v1/owners/" + ownerId + "/moods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ownerId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].scoreMood").value(1));
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testGetMoodsByOwner_WithNoAccess() throws Exception{
        Long ownerId = invalidOwnerId;
        when(moodRepo.getAllByOwner(ownerId)).thenReturn(listMoodFromRepo);

        mockMvc.perform(get("/api/v1/owners/" + ownerId + "/moods")
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
        MoodCreateDTO input = getMoodCreateDTO(ownerId);
        when(moodRepo.save(any(Mood.class))).thenReturn(moodFromRepo);
        when(ownerRepo.getByID(validOwnerId)).thenReturn(Optional.of(owner));

        mockMvc.perform(post("/api/v1/moods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.scoreMood").value(1));

        verify(moodRepo, times(1)).save(any(Mood.class));
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testCreateMood_WithNoAccess() throws Exception {
        Long ownerId = invalidOwnerId;
        MoodCreateDTO input = getMoodCreateDTO(ownerId);
        when(moodRepo.save(any(Mood.class))).thenReturn(moodFromRepo);

        mockMvc.perform(post("/api/v1/moods")
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
        MoodUpdateDTO input = getMoodUpdateDTO();
        when(moodRepo.getByID(moodId)).thenReturn(Optional.of(moodFromRepo));
        when(moodRepo.save(any(Mood.class))).thenReturn(moodFromRepo);
        when(moodAccess.isAllowed(moodId, validOwnerId)).thenReturn(true);

        mockMvc.perform(put("/api/v1/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scoreMood").value(1));

        verify(moodRepo, times(1)).save(any(Mood.class));
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testUpdateMood_WithNoAccess() throws Exception {
        Long moodId = 1L;
        MoodUpdateDTO input = getMoodUpdateDTO();
        when(moodRepo.getByID(moodId)).thenReturn(Optional.of(moodFromRepo));
        when(moodRepo.save(any(Mood.class))).thenReturn(moodFromRepo);
        when(moodAccess.isAllowed(moodId, invalidOwnerId)).thenReturn(false);

        mockMvc.perform(put("/api/v1/moods/" + moodId)
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

        mockMvc.perform(delete("/api/v1/moods/" + moodId)
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

        mockMvc.perform(delete("/api/v1/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(moodId))
                )
                .andExpect(status().isForbidden());

        verify(moodRepo, never()).delete(moodId);
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testCanCreateMood_WithAccess() throws Exception{
        LocalDate date = LocalDate.of(2020, 1, 1);
        when(moodRepo.getByOwnerIdAndCreatedDate(validOwnerId, date)).thenReturn(
                Optional.of(moodFromRepo)
        );

        mockMvc.perform(get("/api/v1/owners/" + validOwnerId + "/can-create-mood")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(validOwnerId))
                        .param("date", date.toString())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed").value("false"));

        verify(moodRepo, times(1)).getByOwnerIdAndCreatedDate(validOwnerId, date);
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testCanCreateEntry_WithNoAccess() throws Exception{
        LocalDate date = LocalDate.of(2020, 1, 1);
        when(moodRepo.getByOwnerIdAndCreatedDate(validOwnerId, date)).thenReturn(
                Optional.of(moodFromRepo)
        );

        mockMvc.perform(get("/api/v1/owners/" + validOwnerId + "/can-create-mood")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(validOwnerId))
                        .param("date", date.toString())
                )
                .andExpect(status().isForbidden());

        verify(moodRepo, never()).getByOwnerIdAndCreatedDate(invalidOwnerId, date);
    }
}
