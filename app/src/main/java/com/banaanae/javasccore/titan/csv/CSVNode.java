package com.banaanae.javasccore.titan.csv;

import com.banaanae.javasccore.titan.Debugger;

import java.util.ArrayList;
import java.util.List;

public class CSVNode {
    String fileName;
    CSVTable table = null;

    public CSVNode(String[] lines, String fileName) {
        this.fileName = fileName;
        this.load(lines);
    }

    public final void load(String[] lines) {
        this.table = new CSVTable(this, lines.length);

        if (lines.length > 2) {
            final List<String> columnNames = parseLine(lines[0]);
            final List<String> columnTypes = parseLine(lines[1]);

            for (String columnName : columnNames)
                table.addColumn(columnName);

            for (int i = 0; i < columnNames.size(); i++) {
                String type = (columnTypes.get(i)).toLowerCase();
                int columnType = -1;

                if (!type.isEmpty()) {
                    type = type.replaceAll("(alt)|(array)|(value)", "");
                    switch (type) {
                        case "string", "text", "hex" -> columnType = 0;
                        case "number", "int" -> columnType = 1;
                        case "boolean" -> columnType = 2;
                        default ->
                                Debugger.error(String.format("CSVNode.load: Invalid column type '%s', column name %s, file %s. Expecting: int/string/boolean. Got: '%s"
                                        , type, columnNames.get(i), fileName, type));
                    }
                }

                table.addColumnType(columnType);
            }

            table.validateColumnTypes();

            for (int i = 2; i < lines.length; i++) {
                final List<String> values = this.parseLine(lines[i]);

                if (!values.isEmpty()) {
                    if (values.getFirst() != null && !values.getFirst().isEmpty())
                        this.table.createRow();

                    for (int j = 0; j < values.size(); j++)
                        this.table.addAndConvertValue(values.get(j), j);
                }
            }
        }
    }

    public List<String> parseLine(String line) {
        boolean inQuote = false;
        StringBuilder readField = new StringBuilder();
        List<String> fields = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            final char currentChar = line.charAt(i);

            if (currentChar == '"') {
                if (inQuote) {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        readField.append('"');
                    } else {
                        inQuote = false;
                    }
                } else {
                    inQuote = true;
                }
            } else if (currentChar == ',' && !inQuote) {
                fields.add(readField.toString());
                readField = new StringBuilder();
            } else {
                readField.append(currentChar);
            }
        }

        fields.add(readField.toString().replace("\r", ""));

        return fields;
    }

    public String getFileName() {
        return this.fileName;
    }

    public CSVTable getTable() {
        return this.table;
    }
}
