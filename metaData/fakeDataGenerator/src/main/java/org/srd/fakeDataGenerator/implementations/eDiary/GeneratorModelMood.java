package org.srd.fakeDataGenerator.implementations.eDiary;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.srd.fakeDataGenerator.model.Model;
import org.srd.fakeDataGenerator.service.GeneratorModel;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Getter
@Order(2)
public class GeneratorModelMood extends GeneratorModel<ModelMood> {
    public GeneratorModelMood(Faker faker, Random random) {
        super(faker, random);
    }

    @Override
    public String getModelName() {
        return "moods";
    }

    @Override
    public List<ModelMood> generate(Map<String, List<? extends Model>> context) {
        return List.of();
    }
}
