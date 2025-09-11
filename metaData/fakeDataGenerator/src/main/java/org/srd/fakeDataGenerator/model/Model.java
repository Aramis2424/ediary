package org.srd.fakeDataGenerator.model;

import java.util.Map;

public abstract class Model {
    public abstract String getName();
    public abstract Map<String, ?> toExport();
}
