package org.srd.fakeDataGenerator.model;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class BaseModel extends Model {
    private final int anyInt;
    private final String anyString;

    public static String getModelName() {
        return "BaseModel";
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> content = new HashMap<>();
        content.put("Number", anyInt);
        content.put("Raw string", anyString);
        return content;
    }
}
