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
@Order(4)
public class GeneratorModelEntry extends GeneratorModel<ModelEntry> {
    public GeneratorModelEntry(Faker faker, Random random) {
        super(faker, random);
    }

    @Override
    public String getModelName() {
        return "entries";
    }

    @Override
    public List<ModelEntry> generate(Map<String, List<? extends Model>> context) {
        return List.of();
    }
}
