package integration.security;

import integration.context.WithMockOwnerDetails;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.EntryTestMother.getEntryInfoDTO1;
import static utils.EntryTestMother.getEntryInfoDTO2;
import static utils.MoodTestMother.getMoodInfoDTO1;
import static utils.MoodTestMother.getMoodInfoDTO2;

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
    private final List<EntryInfoDTO> mockEntries = List.of(
            getEntryInfoDTO1(),
            getEntryInfoDTO2()
    );
    private final List<MoodInfoDTO> mockMoods = List.of(
            getMoodInfoDTO1(),
            getMoodInfoDTO2()
    );

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testGetEntryCards_WithAccess() throws Exception {
        Long diaryId = 1L;
        when(entryService.getAllEntriesByDiary(diaryId)).thenReturn(mockEntries);
        when(moodService.getMoodsByOwner(validOwnerId)).thenReturn(mockMoods);
        when(entryAccess.isDiaryBelongsOwner(diaryId, validOwnerId)).thenReturn(true);

        mockMvc.perform(get("/api/v1/diaries/" + diaryId + "/entry-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Day1"));

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
