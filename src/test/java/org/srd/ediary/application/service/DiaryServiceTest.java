package org.srd.ediary.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.srd.ediary.application.dto.DiaryInfoDTO;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.repository.DiaryRepository;
import org.srd.ediary.domain.repository.EntryRepository;

import java.time.LocalDate;
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
        Diary diary = new Diary(1L, "d1", "about", 2);
        DiaryInfoDTO expected = new DiaryInfoDTO("d1", "about",
                2, LocalDate.now());
        when(diaryRepo.getByID(id)).thenReturn(Optional.of(diary));

        DiaryInfoDTO actual = service.getDiary(1L);

        assertEquals(expected, actual);
    }
}