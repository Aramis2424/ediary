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
import org.srd.ediary.application.dto.*;
import org.srd.ediary.application.security.access.EntryAccess;
import org.srd.ediary.application.security.access.MoodAccess;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Entry;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.EntryRepository;
import org.srd.ediary.domain.repository.MoodRepository;

import java.time.LocalDate;
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
public class EntrySecurityTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EntryAccess entryAccess;
    @MockBean
    private EntryRepository entryRepo;

    private JacksonTester<EntryCreateDTO> creationJson;
    private JacksonTester<EntryUpdateDTO> updateJson;

    private final LocalDate birthdate = LocalDate.of(2000, 1, 1);
    private final Owner owner = new Owner("Ivan", birthdate, "ivan01", "abc123");
    private final Diary diary = new Diary(owner, "d1", "of1");
    private final Entry entryFromRepo = new Entry(diary, "day1", "good1");
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
                .andExpect(jsonPath("$.title").value("day1"))
                .andExpect(jsonPath("$.content").value("good1"));

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
                .andExpect(jsonPath("$[0].title").value("day1"))
                .andExpect(jsonPath("$[0].content").value("good1"));
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
        EntryCreateDTO input = new EntryCreateDTO(diaryId, "day1", "good1");
        when(entryRepo.save(any(Entry.class))).thenReturn(entryFromRepo);
        when(entryAccess.isDiaryBelongsOwner(diaryId, validOwnerId)).thenReturn(true);

        mockMvc.perform(post("/api/v1/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("day1"))
                .andExpect(jsonPath("$.content").value("good1"));

        verify(entryRepo, times(1)).save(any(Entry.class));
        verify(entryAccess, times(1)).isDiaryBelongsOwner(diaryId, validOwnerId);
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testCreateEntry_WithNoAccess() throws Exception {
        Long diaryId = 1L;
        EntryCreateDTO input = new EntryCreateDTO(diaryId, "day1", "good1");
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
        EntryUpdateDTO input = new EntryUpdateDTO("day1", "good1");
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
                .andExpect(jsonPath("$.title").value("day1"))
                .andExpect(jsonPath("$.content").value("good1"));

        verify(entryRepo, times(1)).save(any(Entry.class));
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testUpdateEntry_WithNoAccess() throws Exception {
        Long entryId = 1L;
        EntryUpdateDTO input = new EntryUpdateDTO("day1", "good1");
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
}
