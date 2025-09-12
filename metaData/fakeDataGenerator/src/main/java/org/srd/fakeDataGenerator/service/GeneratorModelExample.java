package org.srd.fakeDataGenerator.service;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import org.srd.fakeDataGenerator.model.Model;
import org.srd.fakeDataGenerator.model.ModelExample;

import java.util.List;
import java.util.Map;

@Service
@Getter
public class GeneratorModelExample extends GeneratorModel {
    private List<ModelExample> models;

    public GeneratorModelExample(Faker faker) {
        super(faker);
    }

    @Override
    public String getModelName() {
        return "ModelExample";
    }

    @Override
    public List<Map<String, Object>> getListMap() {
        return models.stream()
                .map(Model::toMap)
                .toList();
    }

    public void generate() {
        models = List.of(
                new ModelExample(1, faker.name().firstName()),
                new ModelExample(2, faker.name().firstName()),
                new ModelExample(3, faker.name().firstName())
        );
    }
}
