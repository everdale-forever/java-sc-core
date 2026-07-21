package com.banaanae.javasccore.titan.csv;

import com.banaanae.javasccore.titan.Debugger;

import java.util.ArrayList;
import java.util.List;

public class CSVTable {
    private final int BOOLEAN_VALUE_NOT_SET = 0x2;
    private final int INT_VALUE_NOT_SET = Integer.MAX_VALUE;

    List<String> columnNameList = new ArrayList<>();
    List<CSVColumn> columnList = new ArrayList<>();
    List<CSVRow> rowList = new ArrayList<>();

    CSVNode node = null;
    int size = 0;

    public CSVTable(CSVNode node, int size) {
        this.node = node;
        this.size = size;
    }

    public void addAndConvertValue(String value, int columnIndex) {
        final CSVColumn column = this.columnList.get(columnIndex);

        if (column == null) {
            Debugger.error("CSVTable::addAndConvertValue: invalid column index", String.valueOf(columnIndex), this.getFileName());
            return;
        }

        if (value != null && !value.isEmpty()) {
            switch (column.getColumnType()) {
                case -1:
                case 0:
                    column.addStringValue(value);
                    break;
                case 1:
                    column.addIntegerValue(Integer.parseInt(value));
                    break;
                case 2:
                    if (value.equalsIgnoreCase("true") || value.equals("1"))
                        column.addBooleanValue(true);
                    else if (value.equalsIgnoreCase("false") || value.equals("0"))
                        column.addBooleanValue(false);
                    else {
                        Debugger.warning(String.format("CSVTable::addAndConvertValue: Invalid value '%s' in Boolean column '%s', %s"
                                , value, this.columnNameList.get(columnIndex), this.getFileName()));
                    }
            }
        } else
            column.addEmptyValue();
    }

    public void addColumn(String columnName) {
        this.columnNameList.add(columnName);
    }

    public void addColumnType(int columnType) {
        this.columnList.add(new CSVColumn(columnType, this.size));
    }

    public void addRow(CSVRow row) {
        this.rowList.add(row);
    }

    public void columnNamesLoaded() {
    } // Resizes above lists, for accuracy should use arrays but I don't want to go insane

    public void createRow() {
        this.rowList.add(new CSVRow(this));
    }

    public int getArraySizeAt(CSVRow row, int columnIndex) {
        if (!this.rowList.isEmpty()) {
            final int rowId = this.rowList.indexOf(row);

            if (rowId != -1) {
                final CSVColumn column = this.columnList.get(columnIndex);
                return column.getArraySize(this.rowList.get(rowId).getRowOffset(),
                        rowId + 1 >= this.rowList.size()
                                ? column.getSize()
                                : this.rowList.get(rowId + 1).getRowOffset());
            }
        }

        return 0;
    }

    public String getColumnName(int columnIndex) {
        return this.columnNameList.get(columnIndex);
    }

    public int getColumnIndexByName(String name) {
        return this.columnNameList.indexOf(name);
    }

    public int getColumnCount() {
        return this.columnList.size();
    }

    public int getColumnRowCount() {
        return this.columnList.getFirst().getSize();
    }

    public int getColumnTypeCount() {
        return this.columnList.size();
    }

    public String getFileName() {
        return this.node.getFileName();
    }

    public boolean getBooleanValue(String name, int index) {
        return this.getBooleanValueAt(this.getColumnIndexByName(name), index);
    }

    public boolean getBooleanValueAt(int columnIndex, int index) {
        if (columnIndex != -1) {
            return this.columnList.get(columnIndex).getBooleanValue(index);
        }

        return false;
    }

    public int getIntegerValue(String name, int index) {
        return this.getIntegerValueAt(this.getColumnIndexByName(name), index);
    }

    public int getIntegerValueAt(int columnIndex, int index) {
        if (columnIndex != -1) {
            int value = this.columnList.get(columnIndex).getIntegerValue(index);

            if (value == INT_VALUE_NOT_SET)
                value = 0;

            return value;
        }

        return 0;
    }

    public String getValue(String name, int index) {
        return this.getValueAt(this.getColumnIndexByName(name), index);
    }

    public String getValueAt(int columnIndex, int index) {
        if (columnIndex != -1) {
            return this.columnList.get(columnIndex).getStringValue(index);
        }

        return "";
    }

    public int getValueIndex(String name) {
        return this.getColumnIndexByName(name);
    }

    public CSVRow getRowAt(int index) {
        return this.rowList.get(index);
    }

    public CSVRow getRowByName(String name) {
        for (int i = 0; i < getRowCount(); i++) {
            if (name.equalsIgnoreCase(getRowAt(i).getName()))
                return getRowAt(i);
        }
        return null;
    }

    public CSVColumn getCSVColumn(int index) {
        return this.columnList.get(index);
    }

    public int getRowCount() {
        int total = 0;

        for (CSVRow row : this.rowList) {
            total += row.getBiggestArraySize();
        }

        return total;
    }

    public int getRowCountOfCSVRow() {
        return this.rowList.size();
    }

    public void validateColumnTypes() {
        if (this.columnNameList.size() != this.columnList.size()) {
            Debugger.warning(String.format("CSVTable::validateColumnTypes: Column name list length (%d) doesn't match column list length (%d), %s"
                    , this.columnNameList.size(), this.columnList.size(), this.getFileName()));
        }
    }
}
