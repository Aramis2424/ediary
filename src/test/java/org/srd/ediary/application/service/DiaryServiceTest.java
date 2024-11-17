package org.srd.ediary.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.srd.ediary.application.dto.DiaryCreateDTO;
import org.srd.ediary.application.dto.DiaryInfoDTO;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.DiaryRepository;
import org.srd.ediary.domain.repository.EntryRepository;
import org.srd.ediary.domain.repository.OwnerRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

    @Test
    void testGetDiary() {
        Long id = 1L;
        Owner owner = new Owner("Ivan", LocalDate.of(2000, 1, 1),
                "example", "abc123");
        Diary diary = new Diary(owner, "d1", "about");
        DiaryInfoDTO expected = new DiaryInfoDTO(null,"d1", "about",
                0, LocalDate.now());
        when(diaryRepo.getByID(id)).thenReturn(Optional.of(diary));

        DiaryInfoDTO actual = service.getDiary(1L);

        assertEquals(expected, actual);
    }

    @Test
    void testGetOwnerDiaries() {
        Long ownerID = 1L;
        Owner owner = new Owner("Ivan", LocalDate.of(2000, 1, 1),
                "example", "abc123");
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
    void testCreate() {
        DiaryCreateDTO input = new DiaryCreateDTO(1L, "d1", "of1");
        DiaryInfoDTO expected = new DiaryInfoDTO(null,"d1", "of1", 0, LocalDate.now());
        Owner owner = new Owner("Ivan", LocalDate.of(2000, 1, 1),
                "example", "abc123");
        Diary diary = new Diary(owner, "d1", "of1");
        when(ownerRepo.getByID(1L)).thenReturn(Optional.of(owner));
        when(diaryRepo.save(Mockito.any(Diary.class))).thenReturn(diary);

        DiaryInfoDTO actual = service.create(input);

        assertEquals(expected, actual);
    }
}