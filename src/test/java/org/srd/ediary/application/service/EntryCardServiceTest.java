package org.srd.ediary.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.srd.ediary.application.dto.EntryCardDTO;
import org.srd.ediary.application.dto.EntryInfoDTO;
import org.srd.ediary.application.dto.MoodInfoDTO;
import org.srd.ediary.application.security.utils.AuthHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntryCardServiceTest {
    @Mock
    private EntryService entryService;
    @Mock
    private MoodService moodService;
    @Mock
    AuthHelper authHelper;

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
    @InjectMocks
    private EntryCardService service;

    @Test
    void testGetEntryCards_UsualTest() {
        Long diaryId = 1L;
        Long ownerId = 1L;
        when(entryService.getAllEntriesByDiary(diaryId)).thenReturn(mockEntries);
        when(moodService.getMoodsByOwner(ownerId)).thenReturn(mockMoods);
        when(authHelper.getCurrentUserId()).thenReturn(ownerId);
        List<EntryCardDTO> expectedCards = List.of(
                new EntryCardDTO(1L, diaryId, "test 01", 7, 8,
                        LocalDate.of(2020, 1, 1)),
                new EntryCardDTO(2L, diaryId, "test 02", -1, -1,
                        LocalDate.of(2020, 2, 2))
        );

        List<EntryCardDTO> actualCards = service.getEntryCards(diaryId);

        assertEquals(expectedCards, actualCards);
    }
}
