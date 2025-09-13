package org.srd.fakeDataGenerator.service.illustration;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.srd.fakeDataGenerator.model.Model;
import org.srd.fakeDataGenerator.model.ModelExample;
import org.srd.fakeDataGenerator.service.GeneratorModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Getter
@Order(1)
public class GeneratorModelExample extends GeneratorModel<ModelExample> {
    public GeneratorModelExample(Faker faker, Random random) {
        super(faker, random);
    }

    @Override
    public String getModelName() {
        return "examples";
    }


    @Override
    public List<ModelExample> generate(Map<String, List<? extends Model>> context) {
        List<ModelExample> list = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            list.add(new ModelExample(i, "Name" + (random.nextInt(90)+10)));
        }
        System.out.println("ExampleGenerator produced " + list.size() + " items");
        return list;
    }
}
