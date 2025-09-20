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
@Order(1)
public class GeneratorModelOwner extends GeneratorModel<ModelOwner> {
    public GeneratorModelOwner(Faker faker, Random random) {
        super(faker, random);
    }

    @Override
    public String getModelName() {
        return "owners";
    }

    @Override
    public List<ModelOwner> generate(Map<String, List<? extends Model>> context) {
        return List.of();
    }
}
