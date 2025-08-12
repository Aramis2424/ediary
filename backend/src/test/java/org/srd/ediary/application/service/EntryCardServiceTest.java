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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static utils.EntryCardTestMother.getEntryCardDTO1;
import static utils.EntryTestMother.getEntryInfoDTO1;
import static utils.MoodTestMother.getMoodInfoDTO1;

@ExtendWith(MockitoExtension.class)
class EntryCardServiceTest {
    @Mock
    private EntryService entryService;
    @Mock
    private MoodService moodService;
    @Mock
    AuthHelper authHelper;
    @InjectMocks
    private EntryCardService service;

    private final List<EntryInfoDTO> mockEntries = List.of(getEntryInfoDTO1(), getEntryInfoDTO1());
    private final List<MoodInfoDTO> mockMoods = List.of(getMoodInfoDTO1(), getMoodInfoDTO1());

    @Test
    void testGetEntryCards_UsualTest() {
        Long diaryId = 1L;
        Long ownerId = 1L;
        when(entryService.getAllEntriesByDiary(diaryId)).thenReturn(mockEntries);
        when(moodService.getMoodsByOwner(ownerId)).thenReturn(mockMoods);
        when(authHelper.getCurrentUserId()).thenReturn(ownerId);
        List<EntryCardDTO> expectedCards = List.of(getEntryCardDTO1(diaryId), getEntryCardDTO1(diaryId));

        List<EntryCardDTO> actualCards = service.getEntryCards(diaryId);

        assertEquals(expectedCards, actualCards);
    }
}
