package org.srd.fakeDataGenerator.service;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.srd.fakeDataGenerator.model.Model;
import org.srd.fakeDataGenerator.model.ModelExample;
import org.srd.fakeDataGenerator.model.ModelSample;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Getter
public class GeneratorModelSample extends GeneratorModel {
    @Value("${faker.seed:0}")
    private long seed;
    private List<ModelSample> models;
    private final Random random = new Random(seed);

    public GeneratorModelSample(Faker faker) {
        super(faker);
    }

    @Override
    public String getModelName() {
        return "ModelSample";
    }

    @Override
    public List<Map<String, Object>> getListMap() {
        return models.stream()
                .map(Model::toMap)
                .toList();
    }

    public void generate(List<ModelExample> examples) {
        models = List.of(
                new ModelSample(random.nextInt(), examples.getFirst().getAnyString()),
                new ModelSample(random.nextInt(), examples.getLast().getAnyString()),
                new ModelSample(random.nextInt(), examples.get(1).getAnyString())
        );
    }
}
