package org.srd.fakeDataGenerator.implementations.eDiary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.srd.fakeDataGenerator.model.Model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ModelEntry extends Model {
    private final int id;
    private final int diary;
    private final String title;
    private final String content;
    private final LocalDate createdDate;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("entry_id", id);
        map.put("diary_fk", diary);
        map.put("title", title);
        map.put("content", content);
        map.put("created_date", createdDate);
        return map;
    }
}
