package org.srd.fakeDataGenerator.model;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class BaseModel extends Model {
    private final int anyInt;

    @Override
    public String getModelName() {
        return "BaseModel";
    }

    @Override
    public Map<String, ?> toExport() {
        return Map.of("Key", anyInt);
    }
}
