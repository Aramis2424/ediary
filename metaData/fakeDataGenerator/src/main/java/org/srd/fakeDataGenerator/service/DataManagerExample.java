package org.srd.fakeDataGenerator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.srd.fakeDataGenerator.exporter.Exporter;

import java.util.List;

@Service
public class DataManagerExample extends DataManager {
    GeneratorModelExample generatorModelBase;
    GeneratorModelSample generatorModelSample;

    public DataManagerExample(List<? extends Exporter> exporters,
                              ObjectMapper mapper,
                              GeneratorModelExample generatorModelBase,
                              GeneratorModelSample generatorModelSample) {
        super(exporters, mapper);
        this.generatorModelBase = generatorModelBase;
        this.generatorModelSample = generatorModelSample;
    }

    @Override
    protected void generate() {
        generatorModelBase.generate();
        generatorModelSample.generate(generatorModelBase.getModels());
    }

    @Override
    protected void export() {
        exporters.forEach(it -> {
                    it.export(outputDir + generatorModelBase.getModelName(), generatorModelBase.getListMap());
                    it.export(outputDir + generatorModelSample.getModelName(), generatorModelSample.getListMap());
                }
        );
    }
}
