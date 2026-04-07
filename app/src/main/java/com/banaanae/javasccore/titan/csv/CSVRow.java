package com.banaanae.javasccore.titan.csv;

import com.banaanae.javasccore.titan.LogicMath;

public class CSVRow {
    int rowOffset = 0;
    CSVTable table = null;
    
    public CSVRow(CSVTable table) {
        this.table = table;
        this.rowOffset = table.getColumnCount();
    }
    
    public int getArraySize(String columnName) {
        final int columnIndex = this.getColumnIndexByName(columnName);

        if (columnIndex == -1) {
            return 0;
        }

        return this.table.getArraySizeAt(this, columnIndex);
    }

    public int getBiggestArraySize() {
        final int columnCount = this.table.getColumnCount();
        int maxSize = 1;

        for (int i = columnCount - 1; i >= 0; i--) {
            maxSize = LogicMath.max(this.table.getArraySizeAt(this, i), maxSize);
        }

        return maxSize;
    }

    public int getColumnCount() {
        return this.table.getColumnCount();
    }

    public int getColumnIndexByName(String name) {
        return this.table.getColumnIndexByName(name);
    }

    public boolean getBooleanValue(String columnName, int index) {
        return this.table.getBooleanValue(columnName, this.rowOffset + index);
    }

    public boolean getBooleanValueAt(int columnIndex, int index) {
        return this.table.getBooleanValueAt(columnIndex, this.rowOffset + index);
    }

    public boolean getClampedBooleanValueAt(String columnName, int index) {
        final int columnIndex = this.getColumnIndexByName(columnName);

        if (columnIndex != -1) {
            final int arraySize = this.table.getArraySizeAt(this, columnIndex);

            if (index >= arraySize || arraySize < 1) {
                index = LogicMath.max(arraySize - 1, 0);
            }

            return this.table.getBooleanValueAt(columnIndex, this.rowOffset + this.rowOffset + index);
        }

        return false;
    }

    public int getIntegerValue(String columnName, int index) {
        return this.table.getIntegerValue(columnName, this.rowOffset + index);
    }

    public int getIntegerValueAt(int columnIndex, int index) {
        return this.table.getIntegerValueAt(columnIndex, this.rowOffset + index);
    }

    public int getClampedIntegerValueAt(String columnName, int index) {
        final int columnIndex = this.getColumnIndexByName(columnName);

        if (columnIndex != -1) {
            final int arraySize = this.table.getArraySizeAt(this, columnIndex);

            if (index >= arraySize || arraySize < 1) {
                index = LogicMath.max(arraySize - 1, 0);
            }

            return this.table.getIntegerValueAt(columnIndex, this.rowOffset + index);
        }

        return 0;
    }

    public Object getValue(String columnName, int index) {
        return this.table.getValue(columnName, this.rowOffset + index);
    }

    public Object getValueAt(int columnIndex, int index) {
        return this.table.getValueAt(columnIndex, this.rowOffset + index);
    }

    public Object getClampedValueAt(String columnName, int index) {
        final int columnIndex = this.getColumnIndexByName(columnName);

        if (columnIndex != -1) {
            final int arraySize = this.table.getArraySizeAt(this, columnIndex);

            if (index >= arraySize || arraySize < 1) {
                index = LogicMath.max(arraySize - 1, 0);
            }

            return this.table.getValueAt(columnIndex, this.rowOffset + index);
        }

        return "";
    }

    public String getName() {
        return (String) this.table.getValueAt(0, this.rowOffset);
    }

    public int getRowOffset() {
        return this.rowOffset;
    }
}
