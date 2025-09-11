package org.srd.fakeDataGenerator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    final protected List<? extends Exporter> exporters;
    final protected String outputDir = "generatedData/";

    public void run() {
        createDirIfNotExists();
        generate();
        export();
    }

    protected abstract void generate();
    protected abstract void export();

    private void createDirIfNotExists() {
        Path path = Paths.get(outputDir);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error("Cannot create output directory: " + outputDir);
                throw new RuntimeException("Cannot create output directory: " + outputDir);
            }
        }
    }
}
