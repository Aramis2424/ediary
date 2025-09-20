package org.srd.fakeDataGenerator.implementations.eDiary;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.srd.fakeDataGenerator.model.Model;
import org.srd.fakeDataGenerator.service.GeneratorModel;

import java.time.LocalDate;
import java.util.*;

@Service
@Getter
@Order(3)
public class GeneratorModelDiary extends GeneratorModel<ModelDiary> {
    public GeneratorModelDiary(Faker faker, Random random) {
        super(faker, random);
    }

    @Override
    public String getModelName() {
        return "diaries";
    }

    @Override
    public List<ModelDiary> generate(Map<String, List<? extends Model>> context) {
        List<? extends Model> owners = context.getOrDefault("owners", Collections.emptyList());
        List<ModelDiary> out = new ArrayList<>();
        int diaryId = 1;
        for (Model model : owners) {
            if (!(model instanceof ModelOwner owner)) {
                continue;
            }
            int cntDiariesPerOwner = faker.number().numberBetween(0, 2);
            for (int i = 0; i < cntDiariesPerOwner; i++) {
                LocalDate createdDate = Utils.generateLocalDateAfter(owner.getCreatedDate(), 20);

                out.add(
                        new ModelDiary(
                                diaryId,
                                owner.getId(),
                                Utils.generateSentence(3),
                                Utils.generateSentence(5),
                                faker.number().numberBetween(0, 5), createdDate)
                );
                diaryId++;
            }
        }
        return out;
    }
}
