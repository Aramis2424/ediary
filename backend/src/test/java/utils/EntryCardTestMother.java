package utils;

import org.srd.ediary.application.dto.EntryCardDTO;

import java.time.LocalDate;

public class EntryCardTestMother {
    public static EntryCardDTO getEntryCardDTO1(Long diaryId) {
        return new EntryCardDTO(null, diaryId, "Day1", 1, 10, LocalDate.now());
    }
    public static EntryCardDTO getEntryCardDTO2(Long diaryId) {
        return new EntryCardDTO(null, diaryId, "Day2", 2, 9, LocalDate.now());
    }
}
