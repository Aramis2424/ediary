package integration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import integration.context.WithMockOwnerDetails;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.srd.ediary.EdiaryApplication;
import org.srd.ediary.application.dto.*;
import org.srd.ediary.application.security.access.EntryAccess;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Entry;
import org.srd.ediary.domain.repository.EntryRepository;
import org.srd.ediary.domain.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.DiaryTestMother.getDiary1;
import static utils.EntryTestMother.*;

@Epic("Integration Tests")
@Feature("Security")
@SpringBootTest(classes = EdiaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("integration_test")
public class EntrySecurityTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EntryAccess entryAccess;
    @MockBean
    private EntryRepository entryRepo;
    @MockBean
    private DiaryRepository diaryRepo;

    private JacksonTester<EntryCreateDTO> creationJson;
    private JacksonTester<EntryUpdateDTO> updateJson;

    private final Diary diary = getDiary1();
    private final Entry entryFromRepo = getEntry1();
    private final List<Entry> listEntryFromRepo = List.of(entryFromRepo);
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
    void testGetEntry_WithAccess() throws Exception{
        Long entryId = 1L;
        when(entryRepo.getByID(entryId)).thenReturn(Optional.of(entryFromRepo));
        when(entryAccess.isAllowed(entryId, validOwnerId)).thenReturn(true);

        mockMvc.perform(get("/api/v1/entries/" + entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(entryId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Day1"))
                .andExpect(jsonPath("$.content").value("Good day 1"));

        verify(entryAccess, times(1)).isAllowed(entryId, validOwnerId);
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testGetEntry_WithNoAccess() throws Exception{
        Long entryId = 1L;
        when(entryRepo.getByID(entryId)).thenReturn(Optional.of(entryFromRepo));
        when(entryAccess.isAllowed(entryId, invalidOwnerId)).thenReturn(false);

        mockMvc.perform(get("/api/v1/entries/" + entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(entryId))
                )
                .andExpect(status().isForbidden());

        verify(entryAccess, times(1)).isAllowed(entryId, invalidOwnerId);
    }

    @Test
    @WithAnonymousUser
    void testGetEntry_Unauthorized() throws Exception{
        Long entryId = 1L;

        mockMvc.perform(get("/api/v1/entries/" + entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(entryId))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testGetEntriesByDiary_WithAccess() throws Exception{
        Long diaryId = 1L;
        when(entryRepo.getAllByDiary(diaryId)).thenReturn(listEntryFromRepo);
        when(entryAccess.isDiaryBelongsOwner(diaryId, validOwnerId)).thenReturn(true);

        mockMvc.perform(get("/api/v1/diaries/" + diaryId + "/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Day1"))
                .andExpect(jsonPath("$[0].content").value("Good day 1"));
        verify(entryAccess, times(1)).isDiaryBelongsOwner(diaryId, validOwnerId);
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testGetEntriesByDiary_WithNoAccess() throws Exception{
        Long diaryId = 1L;
        when(entryRepo.getAllByDiary(diaryId)).thenReturn(listEntryFromRepo);
        when(entryAccess.isDiaryBelongsOwner(diaryId, invalidOwnerId)).thenReturn(false);

        mockMvc.perform(get("/api/v1/diaries/" + diaryId + "/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                )
                .andExpect(status().isForbidden());
        verify(entryAccess, times(1)).isDiaryBelongsOwner(diaryId, invalidOwnerId);
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testCreateEntry_WithAccess() throws Exception {
        Long diaryId = 1L;
        EntryCreateDTO input = getEntryCreateDTO(diaryId);
        when(entryRepo.save(any(Entry.class))).thenReturn(entryFromRepo);
        when(entryAccess.isDiaryBelongsOwner(diaryId, validOwnerId)).thenReturn(true);
        when(diaryRepo.getByID(diaryId)).thenReturn(Optional.of(diary));

        mockMvc.perform(post("/api/v1/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Day1"))
                .andExpect(jsonPath("$.content").value("Good day 1"));

        verify(entryRepo, times(1)).save(any(Entry.class));
        verify(entryAccess, times(1)).isDiaryBelongsOwner(diaryId, validOwnerId);
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testCreateEntry_WithNoAccess() throws Exception {
        Long diaryId = 1L;
        EntryCreateDTO input = getEntryCreateDTO(diaryId);
        when(entryRepo.save(any(Entry.class))).thenReturn(entryFromRepo);
        when(entryAccess.isDiaryBelongsOwner(diaryId, invalidOwnerId)).thenReturn(false);

        mockMvc.perform(post("/api/v1/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                )
                .andExpect(status().isForbidden());

        verify(entryRepo, never()).save(any(Entry.class));
        verify(entryAccess, times(1)).isDiaryBelongsOwner(diaryId, invalidOwnerId);
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testUpdateEntry_WithAccess() throws Exception {
        Long entryId = 1L;
        EntryUpdateDTO input = getEntryUpdateDTO();
        when(entryRepo.getByID(entryId)).thenReturn(Optional.of(entryFromRepo));
        when(entryRepo.save(any(Entry.class))).thenReturn(entryFromRepo);
        when(entryAccess.isAllowed(entryId, validOwnerId)).thenReturn(true);

        mockMvc.perform(put("/api/v1/entries/" + entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Day2"))
                .andExpect(jsonPath("$.content").value("Good day 2"));

        verify(entryRepo, times(1)).save(any(Entry.class));
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testUpdateEntry_WithNoAccess() throws Exception {
        Long entryId = 1L;
        EntryUpdateDTO input = getEntryUpdateDTO();
        when(entryRepo.getByID(entryId)).thenReturn(Optional.of(entryFromRepo));
        when(entryRepo.save(any(Entry.class))).thenReturn(entryFromRepo);
        when(entryAccess.isAllowed(entryId, invalidOwnerId)).thenReturn(false);

        mockMvc.perform(put("/api/v1/entries/" + entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                )
                .andExpect(status().isForbidden());

        verify(entryRepo, never()).getByID(entryId);
        verify(entryRepo, never()).save(any(Entry.class));
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testDeleteEntry_WithAccess() throws Exception {
        Long entryId = 1L;
        doNothing().when(entryRepo).delete(entryId);
        when(entryAccess.isAllowed(entryId, validOwnerId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/entries/" + entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(entryId))
                )
                .andExpect(status().isOk());

        verify(entryRepo, times(1)).delete(entryId);
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testDeleteEntry_WithNoAccess() throws Exception {
        Long entryId = 1L;
        doNothing().when(entryRepo).delete(entryId);
        when(entryAccess.isAllowed(entryId, invalidOwnerId)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/entries/" + entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(entryId))
                )
                .andExpect(status().isForbidden());

        verify(entryRepo, never()).delete(entryId);
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testCanCreateEntry_WithAccess() throws Exception{
        Long diaryId = 1L;
        LocalDate date = LocalDate.of(2020, 1, 1);
        when(entryRepo.getByDiaryIdAndCreatedDate(diaryId, date)).thenReturn(
                Optional.of(entryFromRepo)
        );
        when(entryAccess.isDiaryBelongsOwner(diaryId, validOwnerId)).thenReturn(true);

        mockMvc.perform(get("/api/v1/diaries/" + diaryId + "/can-create-entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                        .param("date", date.toString())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed").value("false"));

        verify(entryAccess, times(1)).isDiaryBelongsOwner(diaryId, validOwnerId);
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testCanCreateEntry_WithNoAccess() throws Exception{
        Long diaryId = 1L;
        LocalDate date = LocalDate.of(2020, 1, 1);
        when(entryRepo.getByDiaryIdAndCreatedDate(diaryId, date)).thenReturn(
                Optional.of(entryFromRepo)
        );
        when(entryAccess.isDiaryBelongsOwner(diaryId, validOwnerId)).thenReturn(false);

        mockMvc.perform(get("/api/v1/diaries/" + diaryId + "/can-create-entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                        .param("date", date.toString())
                )
                .andExpect(status().isForbidden());

        verify(entryAccess, times(1)).isDiaryBelongsOwner(diaryId, invalidOwnerId);
    }
}
