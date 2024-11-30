package org.srd.ediary.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.srd.ediary.application.dto.EntryCreateDTO;
import org.srd.ediary.application.dto.EntryInfoDTO;
import org.srd.ediary.application.dto.EntryUpdateDTO;
import org.srd.ediary.application.exception.DiaryNotFoundException;
import org.srd.ediary.application.exception.EntryNotFoundException;
import org.srd.ediary.application.exception.OwnerNotFoundException;
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

    @Test
    void testGetEntry_ExistingEntry() {
        final Long entryId = 1L;
        Entry gotEntry = new Entry(diary, "Day1", "Good day");
        EntryInfoDTO expected = new EntryInfoDTO(null, "Day1", "Good day", LocalDate.now());
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
        Entry e1 = new Entry(diary, "day1", "good1");
        Entry e2 = new Entry(diary, "day2", "good2");
        List<Entry> gotEntries = List.of(e1, e2);
        EntryInfoDTO entryDto1 = new EntryInfoDTO(null, "day1", "good1", LocalDate.now());
        EntryInfoDTO entryDto2 = new EntryInfoDTO(null, "day2", "good2", LocalDate.now());
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
        EntryCreateDTO input = new EntryCreateDTO(diaryId, "day1", "good1");
        EntryInfoDTO expected = new EntryInfoDTO(null, "day1", "good1", LocalDate.now());
        Entry createdEntry = new Entry(diary, "day1", "good1");
        when(diaryRepo.getByID(diaryId)).thenReturn(Optional.of(diary));
        when(entryRepo.save(any(Entry.class))).thenReturn(createdEntry);

        EntryInfoDTO actual = service.create(input);

        assertEquals(expected, actual);
    }

    @Test
    void testCreate_WithNonExistingDiary() {
        final Long diaryId = 1L;
        EntryCreateDTO input = new EntryCreateDTO(diaryId, "day1", "good1");
        when(diaryRepo.getByID(diaryId)).thenReturn(Optional.empty());

        assertThrows(DiaryNotFoundException.class, () ->service.create(input));

        verify(entryRepo, never()).save(any(Entry.class));
    }

    @Test
    void testUpdate_ExistingMood() {
        final Long entryId = 1L;
        Entry oldEntry = new Entry(diary, "day1", "good1");
        EntryUpdateDTO updateDTO = new EntryUpdateDTO("day2", "good2");
        Entry updatedEntry = new Entry(diary, "day2", "good2");
        EntryInfoDTO expected = new EntryInfoDTO(null, "day2", "good2", LocalDate.now());
        when(entryRepo.getByID(entryId)).thenReturn(Optional.of(oldEntry));
        when(entryRepo.save(any(Entry.class))).thenReturn(updatedEntry);

        EntryInfoDTO actual = service.update(entryId, updateDTO);

        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_NonExistingMood() {
        final Long entryId = 1L;
        EntryUpdateDTO updateDTO = new EntryUpdateDTO("day2", "good2");
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
}