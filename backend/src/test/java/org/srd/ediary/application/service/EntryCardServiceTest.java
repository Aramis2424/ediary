package org.srd.ediary.application.service;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.srd.ediary.application.dto.EntryCardDTO;
import org.srd.ediary.application.dto.EntryInfoDTO;
import org.srd.ediary.application.dto.MoodInfoDTO;
import org.srd.ediary.application.exception.EntryNotFoundException;
import org.srd.ediary.application.security.utils.AuthHelper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static utils.EntryCardTestMother.*;
import static utils.EntryTestMother.getEntryInfoDTO1;
import static utils.EntryTestMother.getEntryInfoDTO2;
import static utils.MoodTestMother.*;

@Epic("Unit Tests")
@Feature("Business logic")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit_test")
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
    void testGetEntryCards_FromExistingEntries() {
        Long diaryId = 1L;
        Long ownerId = 1L;
        when(entryService.getAllEntriesByDiary(diaryId)).thenReturn(mockEntries);
        when(moodService.getMoodsByOwner(ownerId)).thenReturn(mockMoods);
        when(authHelper.getCurrentUserId()).thenReturn(ownerId);
        List<EntryCardDTO> expectedCards = List.of(getEntryCardDTO1(diaryId), getEntryCardDTO1(diaryId));

        List<EntryCardDTO> actualCards = service.getEntryCards(diaryId);

        assertEquals(expectedCards, actualCards);
    }

    @Test
    void testGetEntryCards_FromNonExistingEntries() {
        Long diaryId = 1L;
        Long ownerId = 1L;
        when(entryService.getAllEntriesByDiary(diaryId)).thenReturn(null);
        when(moodService.getMoodsByOwner(ownerId)).thenReturn(mockMoods);
        when(authHelper.getCurrentUserId()).thenReturn(ownerId);

        assertThrows(EntryNotFoundException.class,
                () -> service.getEntryCards(diaryId));
    }

    @Test
    void testGetEntryCards_WithNonExistingMoods() {
        Long diaryId = 1L;
        Long ownerId = 1L;
        when(entryService.getAllEntriesByDiary(diaryId)).thenReturn(mockEntries);
        when(moodService.getMoodsByOwner(ownerId)).thenReturn(List.of(getMoodInfoDTOWithOldDate()));
        when(authHelper.getCurrentUserId()).thenReturn(ownerId);
        List<EntryCardDTO> expectedCards = List.of(getEntryCardDTO1WithoutMoods(diaryId),
                getEntryCardDTO1WithoutMoods(diaryId));

        List<EntryCardDTO> actualCards = service.getEntryCards(diaryId);

        assertEquals(expectedCards, actualCards);
    }
}
