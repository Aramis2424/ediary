package org.srd.fakeDataGenerator.implementations.eDiary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.srd.fakeDataGenerator.model.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ModelMood extends Model {
    private final int id;
    private final int owner;
    private final int scoreMood;
    private final int scoreProductivity;
    private final LocalDateTime bedtime;
    private final LocalDateTime wakeUpTime;
    private final LocalDate createdDate;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("mood_id", id);
        map.put("owner_fk", owner);
        map.put("score_mood", scoreMood);
        map.put("score_productivity", scoreProductivity);
        map.put("bedtime", bedtime);
        map.put("wake_up_time", wakeUpTime);
        map.put("created_date", createdDate);
        return map;
    }
}
