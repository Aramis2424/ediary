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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.srd.ediary.EdiaryApplication;
import org.srd.ediary.application.config.SecurityConfig;
import org.srd.ediary.application.dto.DiaryCreateDTO;
import org.srd.ediary.application.dto.DiaryUpdateDTO;
import org.srd.ediary.application.security.access.DiaryAccess;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.DiaryRepository;
import org.srd.ediary.domain.repository.OwnerRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EdiaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("integration_test")
public class DiarySecurityTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DiaryAccess diaryAccess;
    @MockBean
    private DiaryRepository diaryRepo;
    @MockBean
    private OwnerRepository ownerRepo;

    private JacksonTester<DiaryCreateDTO> creationJson;
    private JacksonTester<DiaryUpdateDTO> updateJson;

    private final LocalDate birthdate = LocalDate.of(2000, 1, 1);
    private final Owner owner = new Owner("Ivan", birthdate, "ivan01", "abc123");
    private final Diary diaryFromRepo = new Diary(owner, "d1", "of1");
    private final List<Diary> listDiaryFromRepo = List.of(diaryFromRepo);
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
    void testGetDiary_WithAccess() throws Exception{
        Long diaryId = 1L;
        when(diaryRepo.getByID(diaryId)).thenReturn(Optional.of(diaryFromRepo));
        when(diaryAccess.isAllowed(diaryId, validOwnerId)).thenReturn(true);

        mockMvc.perform(get("/api/v1/diaries/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("d1"))
                .andExpect(jsonPath("$.description").value("of1"));

        verify(diaryAccess, times(1)).isAllowed(diaryId, validOwnerId);
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testGetDiary_WithNoAccess() throws Exception{
        Long diaryId = 1L;
        when(diaryRepo.getByID(diaryId)).thenReturn(Optional.of(diaryFromRepo));
        when(diaryAccess.isAllowed(diaryId, invalidOwnerId)).thenReturn(false);

        mockMvc.perform(get("/api/v1/diaries/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                )
                .andExpect(status().isForbidden());

        verify(diaryAccess, times(1)).isAllowed(diaryId, invalidOwnerId);
    }

    @Test
    @WithAnonymousUser
    void testGetDiary_Unauthorized() throws Exception{
        Long diaryId = 1L;

        mockMvc.perform(get("/api/v1/diaries/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testGetDiariesByOwner_WithAccess() throws Exception{
        Long ownerId = validOwnerId;
        when(diaryRepo.getAllByOwner(ownerId)).thenReturn(listDiaryFromRepo);

        mockMvc.perform(get("/api/v1/owners/" + ownerId + "/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ownerId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("d1"))
                .andExpect(jsonPath("$[0].description").value("of1"));
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testGetDiariesByOwner_WithNoAccess() throws Exception{
        Long ownerId = invalidOwnerId;
        when(diaryRepo.getAllByOwner(ownerId)).thenReturn(listDiaryFromRepo);

        mockMvc.perform(get("/api/v1/owners/" + ownerId + "/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ownerId))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testCreateDiary_WithAccess() throws Exception {
        Long ownerId = validOwnerId;
        DiaryCreateDTO input = new DiaryCreateDTO(ownerId, "d1", "of1");
        when(diaryRepo.save(any(Diary.class))).thenReturn(diaryFromRepo);
        when(ownerRepo.getByID(validOwnerId)).thenReturn(Optional.of(owner));

        mockMvc.perform(post("/api/v1/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("d1"))
                .andExpect(jsonPath("$.description").value("of1"));

        verify(diaryRepo, times(1)).save(any(Diary.class));
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testCreateDiary_WithNoAccess() throws Exception {
        Long ownerId = invalidOwnerId;
        DiaryCreateDTO input = new DiaryCreateDTO(ownerId, "d1", "of1");
        when(diaryRepo.save(any(Diary.class))).thenReturn(diaryFromRepo);

        mockMvc.perform(post("/api/v1/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                )
                .andExpect(status().isForbidden());

        verify(diaryRepo, never()).save(any(Diary.class));
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testUpdateDiary_WithAccess() throws Exception {
        Long diaryId = 1L;
        DiaryUpdateDTO input = new DiaryUpdateDTO("d1", "of1");
        when(diaryRepo.getByID(diaryId)).thenReturn(Optional.of(diaryFromRepo));
        when(diaryRepo.save(any(Diary.class))).thenReturn(diaryFromRepo);
        when(diaryAccess.isAllowed(diaryId, validOwnerId)).thenReturn(true);

        mockMvc.perform(put("/api/v1/diaries/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("d1"))
                .andExpect(jsonPath("$.description").value("of1"));

        verify(diaryRepo, times(1)).save(any(Diary.class));
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testUpdateDiary_WithNoAccess() throws Exception {
        Long diaryId = 1L;
        DiaryUpdateDTO input = new DiaryUpdateDTO("d1", "of1");
        when(diaryRepo.getByID(diaryId)).thenReturn(Optional.of(diaryFromRepo));
        when(diaryRepo.save(any(Diary.class))).thenReturn(diaryFromRepo);
        when(diaryAccess.isAllowed(diaryId, invalidOwnerId)).thenReturn(false);

        mockMvc.perform(put("/api/v1/diaries/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                )
                .andExpect(status().isForbidden());

        verify(diaryRepo, never()).getByID(diaryId);
        verify(diaryRepo, never()).save(any(Diary.class));
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testDeleteDiary_WithAccess() throws Exception {
        Long diaryId = 1L;
        doNothing().when(diaryRepo).delete(diaryId);
        when(diaryAccess.isAllowed(diaryId, validOwnerId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/diaries/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                )
                .andExpect(status().isOk());

        verify(diaryRepo, times(1)).delete(diaryId);
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testDeleteDiary_WithNoAccess() throws Exception {
        Long diaryId = 1L;
        doNothing().when(diaryRepo).delete(diaryId);
        when(diaryAccess.isAllowed(diaryId, invalidOwnerId)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/diaries/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                )
                .andExpect(status().isForbidden());

        verify(diaryRepo, never()).delete(diaryId);
    }
}
