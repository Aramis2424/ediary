package org.srd.fakeDataGenerator.model;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class BaseModel extends Model {
    private final int anyInt;

    @Override
    public String getName() {
        return "BaseModel";
    }

    @Override
    public Map<String, ?> toExport() {
        return Map.of("Key", "value");
    }
}
