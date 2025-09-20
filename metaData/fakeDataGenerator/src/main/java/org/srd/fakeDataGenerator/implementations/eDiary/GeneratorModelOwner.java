package org.srd.fakeDataGenerator.implementations.eDiary;

import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.srd.fakeDataGenerator.model.Model;
import org.srd.fakeDataGenerator.service.GeneratorModel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Getter
@Order(1)
public class GeneratorModelOwner extends GeneratorModel<ModelOwner> {
    private final PasswordEncoder encoder;
    public GeneratorModelOwner(Faker faker, Random random, PasswordEncoder encoder) {
        super(faker, random);
        this.encoder = encoder;
    }

    @Override
    public String getModelName() {
        return "owners";
    }

    @Override
    public List<ModelOwner> generate(Map<String, List<?  extends Model>> context) {
        List<ModelOwner> out = new ArrayList<>();
        for (int id = 1; id <= baseGeneratedCount; id++) {
            String name = faker.name().firstName();
            LocalDate birthDate = LocalDate.ofInstant(
                    faker.date().birthday(15, 30).toInstant(),
                    ZoneId.systemDefault()
            );
            String login = faker.funnyName().name().replace(" ", "") + id;
            String password = faker.lordOfTheRings().character().toLowerCase().replace(" ", "") +
                    faker.number().numberBetween(100, 999);
            LocalDate createdDate = LocalDate.of(
                    faker.number().numberBetween(2020, 2024),
                    faker.number().numberBetween(1, 12),
                    faker.number().numberBetween(1, 28)
            );
            out.add(new ModelOwner(id, name, birthDate, login, encoder.encode(password), createdDate));
        }
        return out;
    }
}
