package utils;

import org.srd.ediary.application.dto.MoodCreateDTO;
import org.srd.ediary.application.dto.MoodInfoDTO;
import org.srd.ediary.application.dto.MoodUpdateDTO;
import org.srd.ediary.domain.model.Mood;
import org.srd.ediary.domain.model.Owner;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static utils.OwnerTestFactory.getOwner;

public class MoodTestFactory {
    private static final LocalDateTime bedtime = LocalDateTime
            .of(2020, 1,1, 22,30);
    private static final LocalDateTime wakeUpTime = LocalDateTime
            .of(2020, 1,2, 8,30);

    public static final Owner owner = getOwner();

    public static MoodInfoDTO getMoodInfoDTO1() {
        return new MoodInfoDTO(null, 1, 10, bedtime, wakeUpTime, LocalDate.now());
    }
    public static MoodInfoDTO getMoodInfoDTO2() {
        return new MoodInfoDTO(null, 2, 9, bedtime, wakeUpTime, LocalDate.now());
    }
    public static Mood getMood1() {
        return new Mood(owner, 1, 10, bedtime, wakeUpTime);
    }
    public static Mood getMood2() {
        return new Mood(owner, 2, 9, bedtime, wakeUpTime);
    }
    public static MoodCreateDTO getMoodCreateDTO(Long ownerId) {
        return new MoodCreateDTO(ownerId, 1, 10, bedtime, wakeUpTime);
    }
    public static MoodUpdateDTO getMoodUpdateDTO() {
        return new MoodUpdateDTO(1, 10, bedtime, wakeUpTime);
    }
}
