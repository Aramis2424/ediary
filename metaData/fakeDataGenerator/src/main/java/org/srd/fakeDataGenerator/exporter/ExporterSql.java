package org.srd.fakeDataGenerator.exporter;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Service
@Log4j2
@ConditionalOnProperty(prefix = "config.export.fileType", name = "sql", havingValue = "true")
public class ExporterSql implements Exporter {
    public static String POSTFIX = ".sql";

    @Override
    public void export(String filename, List<Map<String, Object>> objects) {
        Path path = Path.of(filename + POSTFIX);

        try {
            clearFile(path);
        } catch (IOException e) {
            log.error("Cannot create or clear file: {}", filename);
            throw new RuntimeException("Cannot create or clear file: " + filename);
        }

        SqlInsertCommand builder = new SqlInsertCommand(
                filename.split("/")[1],
                objects.getFirst().keySet()
        );

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            for (var object: objects) {
                writer.write(builder.values(object).getString());
                writer.write("\n");
            }
        } catch (IOException ex) {
            log.error("Cannot find file: {}", filename);
            throw new RuntimeException("Cannot find file: " + filename);
        }
    }

    private static class SqlInsertCommand {
        private final String table;
        private final String[] columnsNames;
        private String[] values;

        public SqlInsertCommand(String tableName, Set<String> columnsNames) {
            this.table = tableName;

            int index = 0;
            this.columnsNames = new String[columnsNames.size()];
            for (String colName : columnsNames) {
                this.columnsNames[index++] = colName;
            }
        }

        public SqlInsertCommand values(Map<String, Object> values) {
            int index = 0;
            this.values = new String[values.size()];
            for (Object value : values.values()) {
                if (Objects.requireNonNull(value) instanceof String) {
                    this.values[index++] =
                            "\"" + value + "\"";
                } else {
                    this.values[index++] = value.toString();
                }
            }
            return this;
        }

        public String getString() {
            StringBuilder str = new StringBuilder();
            str.append("INSERT INTO ").append(table);
            str.append(" (");
            insertIterable(str, columnsNames);
            str.append(") ");
            str.append("VALUES ");
            str.append(" (");
            insertIterable(str, values);
            str.append(")");
            str.append(";");
            return str.toString();
        }

        private void insertIterable(StringBuilder str, String[] values) {
            Iterator<String> columnsIterator = Arrays.stream(values).iterator();
            while (columnsIterator.hasNext()) {
                str.append(columnsIterator.next());
                if (columnsIterator.hasNext()) {
                    str.append(", ");
                }
            }
        }
    }
}
