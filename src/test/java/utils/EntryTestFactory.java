package utils;

import org.srd.ediary.application.dto.EntryCreateDTO;
import org.srd.ediary.application.dto.EntryInfoDTO;
import org.srd.ediary.application.dto.EntryUpdateDTO;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Entry;
import org.srd.ediary.domain.model.Owner;

import java.time.LocalDate;

import static utils.DiaryTestFactory.getDiary1;
import static utils.OwnerTestFactory.getOwner;

public class EntryTestFactory {
    public static final Owner owner = getOwner();
    public static final Diary diary = getDiary1();

    public static EntryInfoDTO getEntryInfoDTO1() {
        return new EntryInfoDTO(null, "Day1", "Good day 1", LocalDate.now());
    }
    public static EntryInfoDTO getEntryInfoDTO2() {
        return new EntryInfoDTO(null, "Day2", "Good day 2", LocalDate.now());
    }
    public static Entry getEntry1() {
        return new Entry(diary, "Day1", "Good day 1");
    }
    public static Entry getEntry2() {
        return new Entry(diary, "Day2", "Good day 2");
    }
    public static EntryCreateDTO getEntryCreateDTO(Long diaryId) {
        return new EntryCreateDTO(diaryId, "Day2", "Good day 2");
    }
    public static EntryUpdateDTO getEntryUpdateDTO() {
        return new EntryUpdateDTO("Day2","Good day 2");
    }
}
