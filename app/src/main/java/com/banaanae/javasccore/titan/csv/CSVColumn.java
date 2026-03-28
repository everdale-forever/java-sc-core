package com.banaanae.javasccore.titan.csv;

import java.util.ArrayList;
import java.util.List;

public class CSVColumn {
    private static final int BOOLEAN_VALUE_NOT_SET = 0x2;
    private static final int INT_VALUE_NOT_SET = 0x7FFFFFFF;

    private List<Integer> booleanValues;
    private List<Integer> integerValues;
    private List<String> stringValues;

    private int columnType;

    public CSVColumn(int columnType, int size) {
        this.columnType = columnType;

        this.booleanValues = new ArrayList<>();
        this.integerValues = new ArrayList<>();
        this.stringValues = new ArrayList<>();

        if (columnType < -1 || columnType > 2) {
            throw new IllegalArgumentException("Invalid CSVColumn type: " + columnType);
        }
    }

    public void addEmptyValue() {
        switch (this.columnType) {
            case -1:
            case 0:
                this.stringValues.add("");
                break;
            case 1:
                this.integerValues.add(INT_VALUE_NOT_SET);
                break;
            case 2:
                this.booleanValues.add(BOOLEAN_VALUE_NOT_SET);
                break;
        }
    }

    public void addBooleanValue(boolean value) {
        this.booleanValues.add(value ? 1 : 0);
    }

    public void addIntegerValue(int value) {
        this.integerValues.add(value);
    }

    public void addStringValue(String value) {
        this.stringValues.add(value);
    }

    public int getArraySize(int startOffset, int endOffset) {
        switch (this.columnType) {
            case -1:
            case 0:
                for (int i = endOffset - 1; i + 1 > startOffset; i--) {
                    if (this.stringValues.get(i).length() > 0) {
                        return i - startOffset + 1;
                    }
                }
                break;
            case 1:
                for (int i = endOffset - 1; i + 1 > startOffset; i--) {
                    if (!this.integerValues.get(i).equals(INT_VALUE_NOT_SET)) {
                        return i - startOffset + 1;
                    }
                }
                break;
            case 2:
                for (int i = endOffset - 1; i + 1 > startOffset; i--) {
                    if (!this.booleanValues.get(i).equals(BOOLEAN_VALUE_NOT_SET)) {
                        return i - startOffset + 1;
                    }
                }
                break;
        }
        return 0;
    }

    public boolean getBooleanValue(int index) {
        return this.booleanValues.get(index) == 1;
    }

    public int getIntegerValue(int index) {
        return this.integerValues.get(index);
    }

    public String getStringValue(int index) {
        return this.stringValues.get(index);
    }

    public int getSize() {
        switch (this.columnType) {
            case -1:
            case 0:
                return this.stringValues.size();
            case 1:
                return this.integerValues.size();
            case 2:
                return this.booleanValues.size();
            default:
                return 0;
        }
    }

    public int getColumnType() {
        return this.columnType;
    }
}
