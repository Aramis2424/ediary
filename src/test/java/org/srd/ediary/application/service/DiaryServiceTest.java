package org.srd.ediary.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    void testGetDiary_ExistingDiary() {
        final Long diaryId = 1L;
        Diary diary = new Diary(owner, "d1", "about");
        DiaryInfoDTO expected = new DiaryInfoDTO(null,"d1", "about", 0, LocalDate.now());
        when(diaryRepo.getByID(diaryId)).thenReturn(Optional.of(diary));

        DiaryInfoDTO actual = service.getDiary(1L);

        assertEquals(expected, actual);
    }

    @Test
    void testGetDiary_NonExistingDiary() {
        final Long diaryId = 1L;
        when(diaryRepo.getByID(diaryId)).thenReturn(Optional.empty());

        assertThrows(DiaryNotFoundException.class, () -> service.getDiary(1L));
    }

    @Test
    void testGetOwnerDiaries_ExistingOwner() {
        final Long ownerId = 1L;
        Diary d1 = new Diary(owner, "d1", "of1");
        Diary d2 = new Diary(owner, "d2", "of2");
        Diary d3 = new Diary(owner, "d3", "of3");
        List<Diary> gotDiaries = List.of(d1, d2, d3);
        DiaryInfoDTO dto1 = new DiaryInfoDTO(null,"d1", "of1", 0, LocalDate.now());
        DiaryInfoDTO dto2 = new DiaryInfoDTO(null,"d2", "of2", 0, LocalDate.now());
        DiaryInfoDTO dto3 = new DiaryInfoDTO(null,"d3", "of3", 0, LocalDate.now());
        List<DiaryInfoDTO> expected = List.of(dto1, dto2, dto3);
        when(diaryRepo.getAllByOwner(ownerId)).thenReturn(gotDiaries);

        List<DiaryInfoDTO> actual = service.getOwnerDiaries(ownerId);

        assertEquals(expected, actual);
    }

    @Test
    void testGetOwnerDiaries_NonExistingOwner() {
        final Long ownerId = 1L;
        List<DiaryInfoDTO> expected = List.of();
        when(diaryRepo.getAllByOwner(ownerId)).thenReturn(List.of());

        List<DiaryInfoDTO> actual = service.getOwnerDiaries(ownerId);

        assertEquals(expected, actual);
    }

    @Test
    void testCreate_WithExistingOwner() {
        final Long ownerId = 1L;
        DiaryCreateDTO input = new DiaryCreateDTO(ownerId, "d1", "of1");
        DiaryInfoDTO expected = new DiaryInfoDTO(null,"d1", "of1", 0, LocalDate.now());
        Diary createdDiary = new Diary(owner, "d1", "of1");
        when(ownerRepo.getByID(ownerId)).thenReturn(Optional.of(owner));
        when(diaryRepo.save(any(Diary.class))).thenReturn(createdDiary);

        DiaryInfoDTO actual = service.create(input);

        assertEquals(expected, actual);
    }

    @Test
    void testCreate_WithNonExistingOwner() {
        final Long ownerId = 1L;
        DiaryCreateDTO input = new DiaryCreateDTO(ownerId, "d1", "of1");
        when(ownerRepo.getByID(ownerId)).thenReturn(Optional.empty());

        assertThrows(OwnerNotFoundException.class, () -> service.create(input));

        verify(diaryRepo, never()).save(any(Diary.class));
    }

    @Test
    void testUpdate_ExistingDiary() {
        final Long diaryId = 1L;
        Diary gotDiary = new Diary(owner, "d1", "about");
        DiaryUpdateDTO updateDto = new DiaryUpdateDTO("d2", "of");
        Diary updatedDiary = new Diary(owner, "d2", "of");
        DiaryInfoDTO expected = new DiaryInfoDTO(null,"d2", "of", 0, LocalDate.now());
        when(diaryRepo.getByID(diaryId)).thenReturn(Optional.of(gotDiary));
        when(diaryRepo.save(any(Diary.class))).thenReturn(updatedDiary);

        DiaryInfoDTO actual = service.update(diaryId, updateDto);

        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_NonExistingDiary() {
        final Long diaryId = 1L;
        DiaryUpdateDTO updateDto = new DiaryUpdateDTO("d2", "of");
        when(diaryRepo.getByID(diaryId)).thenReturn(Optional.empty());

        assertThrows(DiaryNotFoundException.class, () -> service.update(diaryId, updateDto));
    }

    @Test
    void testRemove_ExistingDiaryWithEntries() {
        final Long diaryId = 1L;
        Diary removingDiary = new Diary(owner, "d1", "about");
        Entry e1 = new Entry(removingDiary, "e1", "about e1");
        Entry e2 = new Entry(removingDiary, "e2", "about e2");
        List<Entry> diaryEntries = List.of(e1, e2);
        when(entryRepo.getAllByDiary(diaryId)).thenReturn(diaryEntries);
        doNothing().when(entryRepo).delete(any());
        doNothing().when(diaryRepo).delete(diaryId);

        service.remove(diaryId);

        verify(entryRepo, times(2)).delete(any());
        verify(diaryRepo, times(1)).delete(diaryId);
    }

    @Test
    void testRemove_ExistingDiaryWithoutEntries() {
        final Long diaryId = 1L;
        List<Entry> diaryEntries = List.of();
        when(entryRepo.getAllByDiary(diaryId)).thenReturn(diaryEntries);
        doNothing().when(diaryRepo).delete(diaryId);

        service.remove(diaryId);

        verify(entryRepo, never()).delete(any());
        verify(diaryRepo, times(1)).delete(diaryId);
    }

    @Test
    void testRemove_NonExistingDiary() {
        final Long diaryId = 1L;
        List<Entry> diaryEntries = List.of();
        when(entryRepo.getAllByDiary(diaryId)).thenReturn(diaryEntries);
        doNothing().when(diaryRepo).delete(diaryId);

        service.remove(diaryId);

        verify(entryRepo, never()).delete(any());
        verify(diaryRepo, times(1)).delete(diaryId);
    }
}