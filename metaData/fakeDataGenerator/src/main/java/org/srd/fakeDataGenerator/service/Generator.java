package org.srd.fakeDataGenerator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.srd.fakeDataGenerator.exporter.Exporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public abstract class Generator {
    @Value("${config.export.outputDir: generatedData/}")
    protected String outputDir;
    final protected List<? extends Exporter> exporters;
    final protected Faker faker;
    final protected ObjectMapper mapper;

    public void run() {
        createDirIfNotExists();
        generate();
        export();
    }

    protected abstract String getModelName();
    protected abstract void generate();
    protected abstract void export();

    private void createDirIfNotExists() {
        Path path = Paths.get(outputDir);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error("Cannot create output directory: {}", outputDir);
                throw new RuntimeException("Cannot create output directory: " + outputDir);
            }
        }
    }
}
