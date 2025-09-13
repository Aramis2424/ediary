package org.srd.fakeDataGenerator.model.illustration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.srd.fakeDataGenerator.model.Model;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ModelSample extends Model {
    private final int id;
    private final String exampleName;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("ID", id);
        map.put("example_FK", exampleName);
        return map;
    }
}
