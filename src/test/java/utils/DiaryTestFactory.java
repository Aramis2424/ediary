package utils;

import org.srd.ediary.application.dto.DiaryCreateDTO;
import org.srd.ediary.application.dto.DiaryInfoDTO;
import org.srd.ediary.application.dto.DiaryUpdateDTO;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Owner;

import java.time.LocalDate;

import static utils.OwnerTestFactory.getOwner;

public class DiaryTestFactory {
    public static final Owner owner = getOwner();

    public static DiaryInfoDTO getDiaryInfoDTO1() {
        return new DiaryInfoDTO(null,"d1", "about1", 0, LocalDate.now());
    }
    public static DiaryInfoDTO getDiaryInfoDTO2() {
        return new DiaryInfoDTO(null,"d2", "about2", 0, LocalDate.now());
    }
    public static Diary getDiary1() {
        return new Diary(owner, "d1", "about1");
    }
    public static Diary getDiary2() {
        return new Diary(owner, "d2", "about2");
    }
    public static DiaryCreateDTO getDiaryCreateDTO(Long ownerId) {
        return new DiaryCreateDTO(ownerId, "d1", "about1");
    }
    public static DiaryUpdateDTO getDiaryUpdateDTO() {
        return new DiaryUpdateDTO("d2", "about2");
    }
}
