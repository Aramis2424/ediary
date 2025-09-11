package org.srd.fakeDataGenerator.exporter;

import java.util.Map;

public interface Exporter {
    void export(String filename, Map<String, ?> content);
}
