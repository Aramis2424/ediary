package org.srd.fakeDataGenerator.implementations.eDiary;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.srd.fakeDataGenerator.model.Model;
import org.srd.fakeDataGenerator.service.GeneratorModel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
                LocalDate createdDate = generateLocalDateAfter(owner.getCreatedDate(), 20);
                out.add(
                        new ModelDiary(
                                diaryId,
                                owner.getId(),
                                generateSentence(3),
                                generateSentence(5),
                                faker.number().numberBetween(0, 5),
                                createdDate
                        )
                );
                diaryId++;
            }
        }
        return out;
    }

    private String generateSentence(int maxWordsCount) {
        int wordCount = random.nextInt(maxWordsCount) + 1;

        return switch (wordCount) {
            case 2 -> capitalizeFirst(faker.lorem().word() + " " + faker.lorem().word());
            case 3 -> capitalizeFirst(faker.lorem().word() + " " +
                    faker.lorem().word() + " " +
                    faker.lorem().word());
            default -> capitalizeFirst(faker.lorem().word());
        };
    }

    private String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

    private LocalDate generateLocalDateAfter(LocalDate afterDate, int maxDaysAfter) {
        Date startDate = Date.from(afterDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Date futureDate = faker.date().future(maxDaysAfter, TimeUnit.DAYS, startDate);

        return futureDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
