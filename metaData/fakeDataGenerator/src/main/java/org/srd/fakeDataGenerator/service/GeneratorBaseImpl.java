package org.srd.fakeDataGenerator.service;

import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import org.srd.fakeDataGenerator.exporter.Exporter;
import org.srd.fakeDataGenerator.model.BaseModel;
import org.srd.fakeDataGenerator.model.Model;

import java.util.List;
import java.util.Map;

@Service
public class GeneratorBaseImpl extends Generator {
    List<Model> models;

    public GeneratorBaseImpl(List<? extends Exporter> exporters, Faker faker) {
        super(exporters, faker);
    }

    @Override
    protected void generate() {
        models = List.of(
                new BaseModel(1, faker.name().firstName()),
                new BaseModel(2, faker.name().firstName()),
                new BaseModel(3, faker.name().firstName())
        );
    }

    @Override
    protected void export() {
        List<Map<String, Object>> mapList = models.stream()
                        .map(Model::toMap)
                        .toList();
        exporters.forEach(it ->
                it.export(outputDir + BaseModel.getModelName(), mapList)
        );
    }
}
