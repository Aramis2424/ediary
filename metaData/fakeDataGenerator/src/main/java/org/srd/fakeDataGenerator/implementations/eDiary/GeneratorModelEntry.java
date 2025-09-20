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
        List<? extends Model> owners = context.getOrDefault("diaries", Collections.emptyList());
        List<ModelEntry> out = new ArrayList<>();
        int entryId = 1;
        for (Model model : owners) {
            if (!(model instanceof ModelDiary diary)) {
                continue;
            }
            int cntEntries = diary.getCntEntry();
            for (int i = 0; i < cntEntries; i++) {
                LocalDate createdDate = Utils.generateLocalDateAfter(diary.getCreatedDate(), 20);

                out.add(
                        new ModelEntry(
                                entryId,
                                diary.getId(),
                                Utils.generateSentence(1, 3),
                                Utils.generateSentence(10, 20),
                                createdDate)
                );
            }
            entryId++;
        }
        return out;
    }
}
