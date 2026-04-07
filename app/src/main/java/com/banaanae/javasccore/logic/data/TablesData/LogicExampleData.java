package com.banaanae.javasccore.logic.data.TablesData;

import com.banaanae.javasccore.logic.data.LogicData;
import com.banaanae.javasccore.logic.data.LogicDataTable;
import com.banaanae.javasccore.titan.csv.CSVRow;
import java.util.ArrayList;
import java.util.List;

public class LogicExampleData extends LogicData {
    public String stringCol;
    public int intCol;
    public boolean boolCol;
    public List<String> stringArr = new ArrayList<>();
    public List<Integer> intArr = new ArrayList<>();
    public List<Boolean> boolArr = new ArrayList<>();
    
    public LogicExampleData(CSVRow row, LogicDataTable table) {
        super(row, table);
    }
    
    @Override
    public void createReferences() {
        super.createReferences();
        final int arrSize = row.getBiggestArraySize();
        
        this.stringCol = this.getStringValue("StringCol");
        this.intCol = this.getIntValue("IntCol");
        this.boolCol = this.getBoolValue("BoolCol");
        for (int i = 0; i < arrSize; i++) {
            this.stringArr.add(this.getStringArrayValue("StringArr", i));
            this.intArr.add(this.getIntArrayValue("IntArr", i));
            this.boolArr.add(this.getBoolArrayValue("BoolArr", i));
        }
    }

    public String getStringValue(String name) {
        return (String) this.getValue(name, 0);
    }

    public int getIntValue(String name) {
        return this.getIntegerValue(name, 0);
    }

    public boolean getBoolValue(String name) {
        return this.getBooleanValue(name, 0);
    }
    
    public String getStringArrayValue(String name, int index) {
        return (String) this.getValue(name, index);
    }
    
    public int getIntArrayValue(String name, int index) {
        return this.getIntegerValue(name, index);
    }

    public boolean getBoolArrayValue(String name, int index) {
        return this.getBooleanValue(name, index);
    }
}
