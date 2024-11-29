package org.srd.ediary.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.srd.ediary.application.dto.DiaryCreateDTO;
import org.srd.ediary.application.dto.DiaryInfoDTO;
import org.srd.ediary.application.dto.DiaryUpdateDTO;
import org.srd.ediary.application.exception.DiaryNotFoundException;
import org.srd.ediary.application.exception.OwnerNotFoundException;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Entry;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.DiaryRepository;
import org.srd.ediary.domain.repository.EntryRepository;
import org.srd.ediary.domain.repository.OwnerRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.postgresql.hostchooser.HostRequirement.any;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {
    @Mock
    private DiaryRepository diaryRepo;
    @Mock
    private EntryRepository entryRepo;
    @Mock
    private OwnerRepository ownerRepo;
    @InjectMocks
    private DiaryService service;

    private final LocalDate birthdate = LocalDate.of(2000, 1, 1);
    private final Owner owner = new Owner("Ivan", birthdate, "example", "abc123");

    @Test
    void testGetDiaryExisting() {
        Long id = 1L;
        Owner owner = new Owner("Ivan", birthdate, "example", "abc123");
        Diary diary = new Diary(owner, "d1", "about");
        DiaryInfoDTO expected = new DiaryInfoDTO(null,"d1", "about",
                0, LocalDate.now());
        when(diaryRepo.getByID(id)).thenReturn(Optional.of(diary));

        DiaryInfoDTO actual = service.getDiary(1L);

        assertEquals(expected, actual);
    }

    @Test
    void testGetDiaryNonExisting() {
        Long id = 1L;
        when(diaryRepo.getByID(id)).thenReturn(Optional.empty());

        assertThrows(DiaryNotFoundException.class, () -> service.getDiary(1L));
    }

    @Test
    void testGetOwnerDiaries() {
        Long ownerID = 1L;
        Owner owner = new Owner("Ivan", birthdate, "example", "abc123");
        Diary d1 = new Diary(owner, "d1", "of1");
        Diary d2 = new Diary(owner, "d2", "of2");
        Diary d3 = new Diary(owner, "d3", "of3");
        List<Diary> diaries = List.of(d1, d2, d3);
        DiaryInfoDTO dto1 = new DiaryInfoDTO(null,"d1", "of1", 0, LocalDate.now());
        DiaryInfoDTO dto2 = new DiaryInfoDTO(null,"d2", "of2", 0, LocalDate.now());
        DiaryInfoDTO dto3 = new DiaryInfoDTO(null,"d3", "of3", 0, LocalDate.now());
        List<DiaryInfoDTO> expected = List.of(dto1, dto2, dto3);
        when(diaryRepo.getAllByOwner(ownerID)).thenReturn(diaries);

        List<DiaryInfoDTO> actual = service.getOwnerDiaries(ownerID);

        assertEquals(expected, actual);
    }

    @Test
    void testGetOwnerDiariesEmpty() {
        Long ownerID = 1L;
        List<Diary> diaries = List.of();
        List<DiaryInfoDTO> expected = List.of();
        when(diaryRepo.getAllByOwner(ownerID)).thenReturn(diaries);

        List<DiaryInfoDTO> actual = service.getOwnerDiaries(ownerID);

        assertEquals(expected, actual);
    }

    @Test
    void testCreateExistingOwner() {
        DiaryCreateDTO input = new DiaryCreateDTO(1L, "d1", "of1");
        DiaryInfoDTO expected = new DiaryInfoDTO(null,"d1", "of1", 0, LocalDate.now());
        Owner owner = new Owner("Ivan", birthdate, "example", "abc123");
        Diary diary = new Diary(owner, "d1", "of1");
        when(ownerRepo.getByID(1L)).thenReturn(Optional.of(owner));
        when(diaryRepo.save(any(Diary.class))).thenReturn(diary);

        DiaryInfoDTO actual = service.create(input);

        assertEquals(expected, actual);
    }

    @Test
    void testCreateNonExistingOwner() {
        DiaryCreateDTO input = new DiaryCreateDTO(1L, "d1", "of1");
        when(ownerRepo.getByID(1L)).thenReturn(Optional.empty());

        assertThrows(OwnerNotFoundException.class, () -> service.create(input));

        verify(diaryRepo, never()).save(any(Diary.class));
    }

    @Test
    void testUpdateExistingDiary() {
        Long id = 1L;
        Owner owner = new Owner("Ivan", birthdate, "example", "abc123"); // TODO вынести в поле класса
        Diary diary = new Diary(owner, "d1", "about");
        DiaryUpdateDTO updateDiary = new DiaryUpdateDTO("d2", "of");
        Diary diaryUpdated = new Diary(owner, "d2", "of");
        DiaryInfoDTO expected = new DiaryInfoDTO(null,"d2", "of", 0, LocalDate.now());
        when(diaryRepo.getByID(id)).thenReturn(Optional.of(diary));
        when(diaryRepo.save(any(Diary.class))).thenReturn(diaryUpdated);

        DiaryInfoDTO actual = service.update(id, updateDiary);

        assertEquals(expected, actual);
    }

    @Test
    void testUpdateNonExistingDiary() {
        Long id = 1L;
        DiaryUpdateDTO newDiary = new DiaryUpdateDTO("d2", "of");
        when(diaryRepo.getByID(id)).thenReturn(Optional.empty());

        assertThrows(DiaryNotFoundException.class, () -> service.update(id, newDiary));
    }

    @Test
    void testRemoveExistingWithEntries() {
        Long id = 1L;
        Diary diary = new Diary(owner, "d1", "about");
        Entry e1 = new Entry(diary, "e1", "about e1");
        Entry e2 = new Entry(diary, "e2", "about e2");
        List<Entry> entries = List.of(e1, e2);
        when(entryRepo.getAllByDiary(id)).thenReturn(entries);
        doNothing().when(entryRepo).delete(any());
        doNothing().when(diaryRepo).delete(id);

        service.remove(id);

        verify(entryRepo, times(2)).delete(any());
        verify(diaryRepo, times(1)).delete(id);
    }

    @Test
    void testRemoveExistingWithoutEntries() {
        Long id = 1L;
        List<Entry> entries = List.of();
        when(entryRepo.getAllByDiary(id)).thenReturn(entries);
        doNothing().when(diaryRepo).delete(id);

        service.remove(id);

        verify(entryRepo, never()).delete(any());
        verify(diaryRepo, times(1)).delete(id);
    }

    @Test
    void testRemoveNonExisting() {
        Long id = 1L;
        List<Entry> entries = List.of();
        when(entryRepo.getAllByDiary(id)).thenReturn(entries);
        doNothing().when(diaryRepo).delete(id);

        service.remove(id);

        verify(entryRepo, never()).delete(any());
        verify(diaryRepo, times(1)).delete(id);
    }
}