package utils;

import org.srd.ediary.application.dto.EntryCardDTO;

import java.time.LocalDate;

public class EntryCardTestMother {
    public static EntryCardDTO getEntryCardDTO1(Long diaryId) {
        return new EntryCardDTO(null, diaryId, "Day1",
                1, 10, LocalDate.now());
    }
    public static EntryCardDTO getEntryCardDTO2(Long diaryId) {
        return new EntryCardDTO(null, diaryId, "Day2",
                2, 9, LocalDate.now());
    }

    public static EntryCardDTO getEntryCardDTO1WithoutMoods(Long diaryId) {
        return new EntryCardDTO(null, diaryId, "Day1",
                -1, -1, LocalDate.now());
    }
    public static EntryCardDTO getEntryCardDTO2WithoutMoods(Long diaryId) {
        return new EntryCardDTO(null, diaryId, "Day2",
                -1, -1, LocalDate.now());
    }

    public static EntryCardDTO getEntryCardDto1It(Long diaryId) {
        return new EntryCardDTO(1L, diaryId, "Day1",
                1, 10, LocalDate.of(2021, 1, 1));
    }
    public static EntryCardDTO getEntryCardDto2It(Long diaryId) {
        return new EntryCardDTO(2L, diaryId, "Day2",
                2, 9, LocalDate.of(2021, 1, 2));
    }
}
