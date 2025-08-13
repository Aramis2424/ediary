package integration.security;

import integration.security.context.WithMockOwnerDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.srd.ediary.EdiaryApplication;
import org.srd.ediary.application.dto.*;
import org.srd.ediary.application.security.access.EntryAccess;
import org.srd.ediary.application.service.EntryService;
import org.srd.ediary.application.service.MoodService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EdiaryApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("integration_test")
public class EntryCardSecurityTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EntryAccess entryAccess;
    @MockBean
    private EntryService entryService;
    @MockBean
    private MoodService moodService;

    private final static long validOwnerId = 1L;
    private final static long invalidOwnerId = 2L;
    private final LocalDateTime bedtime = LocalDateTime
            .of(2020, 1,1, 22,30);
    private final LocalDateTime wakeUpTime = LocalDateTime
            .of(2020, 1,2, 8,30);

    private final List<EntryInfoDTO> mockEntries = List.of(
            new EntryInfoDTO(1L, "test 01", "Testing 01", LocalDate.of(2020, 1, 1)),
            new EntryInfoDTO(2L, "test 02", "Testing 02", LocalDate.of(2020, 2, 2))
    );
    private final List<MoodInfoDTO> mockMoods = List.of(
            new MoodInfoDTO(1L, 7, 8, bedtime, wakeUpTime, LocalDate.of(2020, 1, 1)),
            new MoodInfoDTO(1L, 9, 10, bedtime, wakeUpTime, LocalDate.of(2020, 3, 3))
    );

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testGetEntryCards_WithAccess() throws Exception {
        Long diaryId = 1L;
        when(entryService.getAllEntriesByDiary(diaryId)).thenReturn(mockEntries);
        when(moodService.getMoodsByOwner(validOwnerId)).thenReturn(mockMoods);
        when(entryAccess.isDiaryBelongsOwner(diaryId, validOwnerId)).thenReturn(true);
        List<EntryCardDTO> expectedCards = List.of(
                new EntryCardDTO(1L, diaryId, "test 01", 7, 8,
                        LocalDate.of(2020, 1, 1)),
                new EntryCardDTO(2L, diaryId, "test 02", -1, -1,
                        LocalDate.of(2020, 2, 2))
        );

        mockMvc.perform(get("/api/v1/diaries/" + diaryId + "/entry-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].entryId").value(1L))
                .andExpect(jsonPath("$[0].title").value("test 01"));

        verify(entryAccess, times(1)).isDiaryBelongsOwner(diaryId, validOwnerId);
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testGetEntryCards_WithNoAccess() throws Exception {
        Long diaryId = 1L;
        when(entryAccess.isDiaryBelongsOwner(diaryId, invalidOwnerId)).thenReturn(false);

        mockMvc.perform(get("/api/v1/diaries/" + diaryId + "/entry-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                )
                .andExpect(status().isForbidden());

        verify(entryAccess, times(1)).isDiaryBelongsOwner(diaryId, invalidOwnerId);
    }
}
