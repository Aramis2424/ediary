package org.srd.fakeDataGenerator.service;

import org.springframework.stereotype.Service;
import org.srd.fakeDataGenerator.exporter.Exporter;
import org.srd.fakeDataGenerator.model.BaseModel;
import org.srd.fakeDataGenerator.model.Model;

import java.util.List;

@Service
public class GeneratorBaseImpl extends Generator {
    List<Model> models;

    public GeneratorBaseImpl(List<? extends Exporter> exporters) {
        super(exporters);
    }

    @Override
    protected void generate() {
        models = List.of(new BaseModel(1), new BaseModel(2));
    }

    @Override
    protected void export() {
        exporters.forEach(it -> {
            models.forEach(m -> it.export(outputDir + m.getModelName(), m.toExport()));
        });
    }
}
