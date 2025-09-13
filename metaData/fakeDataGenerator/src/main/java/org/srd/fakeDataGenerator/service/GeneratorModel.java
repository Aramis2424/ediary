package org.srd.fakeDataGenerator.service;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import org.srd.fakeDataGenerator.model.Model;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public abstract class GeneratorModel<T extends Model> {
    protected final Faker faker;
    protected final Random random;

    public abstract String getModelName();
    public abstract List<T> generate(Map<String, List<? extends Model>> context);
}
