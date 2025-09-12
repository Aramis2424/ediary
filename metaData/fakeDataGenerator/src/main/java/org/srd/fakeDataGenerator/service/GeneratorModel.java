package org.srd.fakeDataGenerator.service;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public abstract class GeneratorModel {
    final protected Faker faker;

    public abstract String getModelName();
    public abstract List<Map<String, Object>> getListMap();
}
