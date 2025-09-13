package org.srd.fakeDataGenerator.service.illustration;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.srd.fakeDataGenerator.model.Model;
import org.srd.fakeDataGenerator.model.ModelSample;
import org.srd.fakeDataGenerator.service.GeneratorModel;

import java.util.*;

@Service
@Getter
@Order(2)
public class GeneratorModelSample extends GeneratorModel<ModelSample> {
    public GeneratorModelSample(Faker faker, Random random) {
        super(faker, random);
    }

    @Override
    public String getModelName() {
        return "samples";
    }

    @Override
    public List<ModelSample> generate(Map<String, List<? extends Model>> context) {
        List<? extends Model> examples = context.getOrDefault("examples", Collections.emptyList());
        List<ModelSample> out = new ArrayList<>();
        for (Model m : examples) {
            Map<String,Object> mm = m.toMap();
            int baseId = (int) mm.get("Number");
            for (int k = 0; k < 2; k++) {
                out.add(new ModelSample(baseId, "note-" + random.nextInt(100)));
            }
        }
        System.out.println("SampleGenerator produced " + out.size() + " items (from base size " + examples.size() + ")");
        return out;
    }
}
