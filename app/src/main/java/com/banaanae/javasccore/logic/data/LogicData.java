package com.banaanae.javasccore.logic.data;

import com.banaanae.javasccore.titan.Debugger;
import com.banaanae.javasccore.titan.GlobalID;
import com.banaanae.javasccore.titan.csv.CSVRow;

public class LogicData {
    int globalId = 0;
    
    public CSVRow row = null;
    LogicDataTable table = null;
    
    public final class LogicDataType {
        // public static final int CSVNAME = [CSV ID];
        
        private LogicDataType() {}
    }
    
    public LogicData(CSVRow row, LogicDataTable table) {
        this.row = row;
        this.table = table;
        
        this.globalId = GlobalID.createGlobalId(table.getTableIndex(), table.getItemCount());
    }
    
    public void createReferences() {}
    public void createReferences2() {}
    
    public void setCSVRow(CSVRow row) {
        this.row = row;
    }
    
    public int getArraySize(String columnName) {
        return this.row.getArraySize(columnName);
    }
    
    public int getDataType() {
        return this.table.getTableIndex();
    }
    
    public int getGlobalId() {
        return globalId;
    }
    
    public int getInstanceId() {
        return GlobalID.getInstanceId(globalId);
    }
    
    public int getColumnIndex(String name) {
        final int columnIndex = row.getColumnIndexByName(name);
        
        if (columnIndex == -1)
            Debugger.warning(String.format("Unable to find column %s from %s", name, getDebuggerName()));
        
        return columnIndex;
    }
    
    public String getDebuggerName() {
        return String.format("%s (%s)", row.getName(), table.getTableName());
    }
    
    public boolean getBooleanValue(String columnName, int index) {
        return this.row.getBooleanValue(columnName, index);
    }

    public boolean getClampedBooleanValue(String columnName, int index) {
        return this.row.getClampedBooleanValueAt(columnName, index);
    }

    public int getIntegerValue(String columnName, int index) {
        return this.row.getIntegerValue(columnName, index);
    }

    public int getClampedIntegerValue(String columnName, int index) {
        return this.row.getClampedIntegerValueAt(columnName, index);
    }

    public Object getValue(String columnName, int index) {
        return this.row.getValue(columnName, index);
    }

    public Object getClampedValue(String columnName, int index) {
        return this.row.getClampedValueAt(columnName, index);
    }

    public String getName() {
        return this.row.getName();
    }
}
