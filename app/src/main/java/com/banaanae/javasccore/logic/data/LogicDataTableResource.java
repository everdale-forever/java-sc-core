package com.banaanae.javasccore.logic.data;

public class LogicDataTableResource {
    String fileName = "";
    int tableIndex = 0;
    int type = 0;
    
    public LogicDataTableResource(String fileName, int tableIndex, int type) {
        this.fileName = fileName;
        this.tableIndex = tableIndex;
        this.type = type;
    }

    public String getFileName() {
        return this.fileName;
    }
    
    public int getTableIndex() {
        return this.tableIndex;
    }

    public int getTableType() {
        return this.type;
    }
}
