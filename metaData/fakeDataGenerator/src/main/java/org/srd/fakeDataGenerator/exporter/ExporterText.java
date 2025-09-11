package org.srd.fakeDataGenerator.exporter;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
@Log4j2
@ConditionalOnProperty(prefix = "config.export.fileType", name = "txt", havingValue = "true")
public class ExporterText implements Exporter {
    @Override
    public void export(String filename, Map<String, ?> content) {
        String postfix = ".txt";
        Path path = Path.of(filename + postfix);

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (var entry : content.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
            }
        } catch (IOException ex) {
            log.error("Cannot find file: {}", filename);
            throw new RuntimeException("Cannot find file: " + filename);
        }
    }
}
