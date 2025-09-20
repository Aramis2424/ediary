package org.srd.fakeDataGenerator.implementations.eDiary;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.srd.fakeDataGenerator.model.Model;
import org.srd.fakeDataGenerator.service.GeneratorModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
        List<? extends Model> owners = context.getOrDefault("owners", Collections.emptyList());
        List<ModelMood> out = new ArrayList<>();
        int moodId = 1;
        for (Model model : owners) {
            if (!(model instanceof ModelOwner owner)) {
                continue;
            }
            int cntMoodsPerOwner = faker.number().numberBetween(0, 5);
            for (int i = 0; i < cntMoodsPerOwner; i++) {
                int ownerId = owner.getId();
                int scoreMood = faker.number().numberBetween(1, 10);
                int scoreProductivity = faker.number().numberBetween(1, 10);
                LocalDate createdDate = generateLocalDateAfter(owner.getCreatedDate(), 20);
                LocalDateTime bedtime = LocalDateTime.of(createdDate, getTimeBetweenHours(0, 4));
                LocalDateTime wakeUpTime = LocalDateTime.of(createdDate, getTimeBetweenHours(7, 10));
                out.add(
                        new ModelMood(moodId, ownerId, scoreMood, scoreProductivity, bedtime, wakeUpTime, createdDate)
                );
                moodId++;
            }
        }
        return out;
    }

    private LocalDate generateLocalDateAfter(LocalDate afterDate, int maxDaysAfter) {
        Date startDate = Date.from(afterDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Date futureDate = faker.date().future(maxDaysAfter, TimeUnit.DAYS, startDate);

        return futureDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private LocalTime getTimeBetweenHours(int from, int to) {
        return LocalTime.of(
                faker.number().numberBetween(from, to),
                faker.number().numberBetween(10, 50)
        );
    }
}
