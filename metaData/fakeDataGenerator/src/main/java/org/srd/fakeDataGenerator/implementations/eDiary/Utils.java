package org.srd.fakeDataGenerator.implementations.eDiary;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class Utils {
    private static Faker faker;
    private static Random random;

    static LocalDate generateLocalDateAfter(LocalDate afterDate, int maxDaysAfter) {
        Date startDate = Date.from(afterDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Date futureDate = faker.date().future(maxDaysAfter, TimeUnit.DAYS, startDate);

        return futureDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    static LocalTime getTimeBetweenHours(int from, int to) {
        return LocalTime.of(
                faker.number().numberBetween(from, to),
                faker.number().numberBetween(10, 50)
        );
    }

    static String generateSentence(int maxWordsCount) {
        int wordCount = random.nextInt(maxWordsCount) + 1;

        return switch (wordCount) {
            case 2 -> capitalizeFirst(faker.lorem().word() + " " + faker.lorem().word());
            case 3 -> capitalizeFirst(faker.lorem().word() + " " +
                    faker.lorem().word() + " " +
                    faker.lorem().word());
            default -> capitalizeFirst(faker.lorem().word());
        };
    }

    static String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
