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
import org.srd.ediary.domain.repository.DiaryRepository;
import org.srd.ediary.domain.repository.EntryRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
    @InjectMocks
    private DiaryService service;

    @Test
    void testGetDiary() {
        Long id = 1L;
        Diary diary = new Diary(1L, "d1", "about");
        DiaryInfoDTO expected = new DiaryInfoDTO("d1", "about",
                0, LocalDate.now());
        when(diaryRepo.getByID(id)).thenReturn(Optional.of(diary));

        DiaryInfoDTO actual = service.getDiary(1L);

        assertEquals(expected, actual);
    }

    @Test
    void testGetOwnerDiaries() {
        Long ownerID = 1L;
        Diary d1 = new Diary(ownerID, "d1", "of1");
        Diary d2 = new Diary(ownerID, "d2", "of2");
        Diary d3 = new Diary(ownerID, "d3", "of3");
        List<Diary> diaries = List.of(d1, d2, d3);
        DiaryInfoDTO dto1 = new DiaryInfoDTO("d1", "of1", 0, LocalDate.now());
        DiaryInfoDTO dto2 = new DiaryInfoDTO("d2", "of2", 0, LocalDate.now());
        DiaryInfoDTO dto3 = new DiaryInfoDTO("d3", "of3", 0, LocalDate.now());
        List<DiaryInfoDTO> expected = List.of(dto1, dto2, dto3);
        when(diaryRepo.getAllByOwnerID(ownerID)).thenReturn(diaries);

        List<DiaryInfoDTO> actual = service.getOwnerDiaries(ownerID);

        assertEquals(expected, actual);
    }

    @Test
    void create() {
        DiaryCreateDTO input = new DiaryCreateDTO(1L, "d1", "of1");
        DiaryInfoDTO expected = new DiaryInfoDTO("d1", "of1", 0, LocalDate.now());
        when(diaryRepo.save(Mockito.any(Diary.class))).thenReturn(Mockito.any(Diary.class));

        DiaryInfoDTO actual = service.create(input);

        assertEquals(expected, actual);
    }
}