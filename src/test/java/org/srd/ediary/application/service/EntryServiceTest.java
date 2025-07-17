package org.srd.ediary.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.srd.ediary.application.dto.EntryCreateDTO;
import org.srd.ediary.application.dto.EntryInfoDTO;
import org.srd.ediary.application.dto.EntryPermission;
import org.srd.ediary.application.dto.EntryUpdateDTO;
import org.srd.ediary.application.exception.DiaryNotFoundException;
import org.srd.ediary.application.exception.EntryNotFoundException;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Entry;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.DiaryRepository;
import org.srd.ediary.domain.repository.EntryRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntryServiceTest {
    @Mock
    private EntryRepository entryRepo;
    @Mock
    private DiaryRepository diaryRepo;
    @InjectMocks
    private EntryService service;

    private final LocalDate birthdate = LocalDate.of(2000, 1, 1);
    private final Owner owner = new Owner("Ivan", birthdate, "example", "abc123");
    private final Diary diary = new Diary(owner, "d1", "about");

    private EntryInfoDTO getEntryInfoDTO1() {
        return new EntryInfoDTO(null, "Day1", "Good day 1", LocalDate.now());
    }

    private EntryInfoDTO getEntryInfoDTO2() {
        return new EntryInfoDTO(null, "Day2", "Good day 2", LocalDate.now());
    }

    private Entry getEntry1() {
        return new Entry(diary, "Day1", "Good day 1");
    }

    private Entry getEntry2() {
        return new Entry(diary, "Day2", "Good day 2");
    }

    private EntryCreateDTO getEntryCreateDTO(Long diaryId) {
        return new EntryCreateDTO(diaryId, "Day2", "Good day 2");
    }
    private EntryUpdateDTO getEntryUpdateDTO() {
        return new EntryUpdateDTO("Day2","Good day 2");
    }

    @Test
    void testGetEntry_ExistingEntry() {
        final Long entryId = 1L;
        Entry gotEntry = getEntry1();
        EntryInfoDTO expected = getEntryInfoDTO1();
        when(entryRepo.getByID(entryId)).thenReturn(Optional.of(gotEntry));

        EntryInfoDTO actual = service.getEntry(entryId);

        assertEquals(expected, actual);
    }

    @Test
    void testGetEntry_NonExistingEntry() {
        final Long entryId = 1L;
        when(entryRepo.getByID(entryId)).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class, () -> service.getEntry(entryId));
    }

    @Test
    void testGetAllEntriesByDiary_ExistingDiary() {
        final Long diaryId = 1L;
        Entry e1 = getEntry1();
        Entry e2 = getEntry2();
        List<Entry> gotEntries = List.of(e1, e2);
        EntryInfoDTO entryDto1 = getEntryInfoDTO1();
        EntryInfoDTO entryDto2 = getEntryInfoDTO2();
        List<EntryInfoDTO> expected = List.of(entryDto1, entryDto2);
        when(entryRepo.getAllByDiary(diaryId)).thenReturn(gotEntries);

        List<EntryInfoDTO> actual = service.getAllEntriesByDiary(diaryId);

        assertEquals(expected, actual);
    }

    @Test
    void testGetAllEntriesByDiary_NonExistingDiary() {
        final Long diaryId = 1L;
        List<EntryInfoDTO> expected = List.of();
        when(entryRepo.getAllByDiary(diaryId)).thenReturn(List.of());

        List<EntryInfoDTO> actual = service.getAllEntriesByDiary(diaryId);

        assertEquals(expected, actual);
    }

    @Test
    void testCreate_WithExistingDiary() {
        final Long diaryId = 1L;
        EntryCreateDTO input = getEntryCreateDTO(diaryId);
        EntryInfoDTO expected = getEntryInfoDTO1();
        Entry createdEntry = getEntry1();
        when(diaryRepo.getByID(diaryId)).thenReturn(Optional.of(diary));
        when(entryRepo.save(any(Entry.class))).thenReturn(createdEntry);

        EntryInfoDTO actual = service.create(input);

        assertEquals(expected, actual);
    }

    @Test
    void testCreate_WithNonExistingDiary() {
        final Long diaryId = 1L;
        EntryCreateDTO input = getEntryCreateDTO(diaryId);
        when(diaryRepo.getByID(diaryId)).thenReturn(Optional.empty());

        assertThrows(DiaryNotFoundException.class, () ->service.create(input));

        verify(entryRepo, never()).save(any(Entry.class));
    }

    @Test
    void testUpdate_ExistingMood() {
        final Long entryId = 1L;
        Entry oldEntry = getEntry1();
        EntryUpdateDTO updateDTO = getEntryUpdateDTO();
        Entry updatedEntry = getEntry2();
        EntryInfoDTO expected = getEntryInfoDTO2();
        when(entryRepo.getByID(entryId)).thenReturn(Optional.of(oldEntry));
        when(entryRepo.save(any(Entry.class))).thenReturn(updatedEntry);

        EntryInfoDTO actual = service.update(entryId, updateDTO);

        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_NonExistingMood() {
        final Long entryId = 1L;
        EntryUpdateDTO updateDTO = getEntryUpdateDTO();
        when(entryRepo.getByID(entryId)).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class, () -> service.update(entryId, updateDTO));

    }

    @Test
    void testDelete_ExistingEntry() {
        final Long entryId = 1L;
        doNothing().when(entryRepo).delete(entryId);

        service.delete(entryId);

        verify(entryRepo, times(1)).delete(entryId);
    }

    @Test
    void testCanCreateEntry_True() {
        final Long diaryId = 1L;
        final LocalDate date = LocalDate.of(2020, 1, 1);
        Entry gotEntry = getEntry1();
        EntryPermission expected = new EntryPermission(false);
        when(entryRepo.getByDiaryIdAndCreatedDate(diaryId, date))
                .thenReturn(Optional.of(gotEntry));

        EntryPermission actual = service.canCreateEntry(diaryId, date);

        assertEquals(expected, actual);
    }

    @Test
    void testCanCreateEntry_False() {
        final Long diaryId = 1L;
        final LocalDate date = LocalDate.of(2020, 1, 1);
        EntryPermission expected = new EntryPermission(true);
        when(entryRepo.getByDiaryIdAndCreatedDate(diaryId, date))
                .thenReturn(Optional.empty());

        EntryPermission actual = service.canCreateEntry(diaryId, date);

        assertEquals(expected, actual);
    }
}
