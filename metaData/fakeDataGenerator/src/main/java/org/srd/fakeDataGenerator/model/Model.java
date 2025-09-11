package org.srd.fakeDataGenerator.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public abstract class Model {
    public abstract Map<String, Object> toMap();
    public abstract Map<String, Object> toMapByFieldNames(ObjectMapper mapper);
}
