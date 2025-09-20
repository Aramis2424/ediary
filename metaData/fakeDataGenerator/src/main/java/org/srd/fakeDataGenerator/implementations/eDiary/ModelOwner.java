package org.srd.fakeDataGenerator.implementations.eDiary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.srd.fakeDataGenerator.model.Model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ModelOwner extends Model {
    private final int id;
    private final String name;
    private final LocalDate birthDate;
    private final String login;
    private final String password;
    private final LocalDate createdDate;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("owner_id", id);
        map.put("name", name);
        map.put("birth_date", birthDate);
        map.put("login", login);
        map.put("password", password);
        map.put("created_date", createdDate.toString());
        return map;
    }
}
