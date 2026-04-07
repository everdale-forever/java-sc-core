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
            
            for (int i = 0; i < columnNames.size(); i++)
                table.addColumn(columnNames.get(i));
            
            for (int i = 0; i < columnNames.size(); i++) {
                final String type = columnTypes.get(i).toLowerCase();
                int columnType = -1;
                
                if (type != null && type.length() > 0) {
                    switch (type) {
                        case "string", "stringarray" -> columnType = 0;
                        case "number", "int", "intarray" -> columnType = 1;
                        case "boolean", "booleanarray" -> columnType = 2;
                        default -> Debugger.error(String.format("CSVNode.load: Invalid column type '%s', column name %s, file %s. Expecting: int/string/boolean. Got: '%s"
                                , type, columnNames.get(i), fileName, type));
                    }
                }
                
                table.addColumnType(columnType);
            }
            
            table.validateColumnTypes();
            
            for (int i = 2; i < lines.length; i++) {
                final List<String> values = this.parseLine(lines[i]);

                if (!values.isEmpty()) {
                    if (values.getFirst() != null && values.getFirst().length() > 0)
                        this.table.createRow();

                    for (int j = 0; j < values.size(); j++)
                        this.table.addAndConvertValue(values.get(j), j);
                }
            }
        }
    }
    
    public List<String> parseLine(String line) {
        boolean inQuote = false;
        String readField = "";
        List<String> fields = new ArrayList<>();
        
        for (int i = 0; i < line.length(); i++) {
            final char currentChar = line.charAt(i);
            
            if (currentChar == '"') {
                if (inQuote) {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"')
                        readField += '"';
                    else
                        inQuote = false;
                } else if (currentChar == ',' && !inQuote) {
                    fields.add(readField);
                    readField = "";
                }
            }
        }
        
        fields.add(readField.replace("\r", ""));
        
        return fields;
    }
    
    public String getFileName() {
        return this.fileName;
    }

    public CSVTable getTable () {
        return this.table;
    }
}
