package com.banaanae.javasccore.logic.data;

import com.banaanae.javasccore.titan.Debugger;
import com.banaanae.javasccore.titan.GlobalID;
import com.banaanae.javasccore.titan.csv.CSVRow;
import com.banaanae.javasccore.titan.csv.CSVTable;
import java.util.ArrayList;
import java.util.List;

public class LogicDataTable {
    int tableIndex = 0;
    String tableName = "";
    boolean loaded = false;
    boolean loaded2 = false;

    CSVTable table;
    List<LogicData> items;

    public LogicDataTable(CSVTable table, int index) {
        this.tableIndex = index;
        this.table = table;
        this.items = new ArrayList<>();

        loadTable();
    }

    public final void loadTable() {
        for (int i = 0; i < this.table.getRowCount(); i++) {
            this.addItem(this.table.getRowAt(i));
        }
    }

    public void setTable(CSVTable table) {
        this.table = table;

        for (int i = 0; i < this.items.size(); i++) {
            this.items.get(i).setCSVRow(table.getRowAt(i));
        }
    }

    public void addItem(CSVRow row) { 
        this.items.add(this.createItem(row));
    }

    public LogicData createItem(CSVRow row) {
        LogicData data = null;

        switch (this.tableIndex) {
            /*
            case LogicDataType.EXAMPLE:
                data = new LogicExampleData(row, this);
                break;
            */

            default:
                Debugger.error("Invalid data table index: " + this.tableIndex);
        }

        return data;
    }

    public void createReferences() {
        if (!this.loaded) {
            for (int i = 0; i < this.items.size(); i++) {
                if (this.items.get(i) == null) {
                    Debugger.error(String.format("LogicDataTable::createReferences() - Item at table (%s) is null! Row: %d"
                            , this.tableIndex, i));
                    continue;
                }
                this.items.get(i).createReferences();
            }

            this.loaded = true;
        }
    }

    public void createReferences2() {
        if (!this.loaded2) {
            for (int i = 0; i < this.items.size(); i++) {
                if (this.items.get(i) == null)
                    continue;
                this.items.get(i).createReferences2();
            }

            this.loaded2 = true;
        }
    }

    public LogicData getItemAt(int index) {
        return this.items.get(index);
    }

    public LogicData getDataByName(String name) {
        if (name != null && name.length() > 0) {
            for (int i = 0; i < this.items.size(); i++) {
                final LogicData data = this.items.get(i);

                if (data.getName().equals(name))
                    return data;
            }
 
            Debugger.warning("CSV row has an invalid name:", name);
        }

        return null;
    }

    public Object getItemById(int globalId) {
        final int instanceId = GlobalID.getInstanceId(globalId);

        if (instanceId < 0 || instanceId >= this.items.size()) {
            Debugger.warning(String.format("LogicDataTable::getItemById() - Instance id out of bounds! %d / %d"
                    , instanceId + 1, items.size()));
            return null;
        }

        return this.items.get(instanceId);
    }

    public int getItemCount() {
        return this.items.size();
    }

    public int getTableIndex() {
        return this.tableIndex;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setName(String name) {
        this.tableName = name;
    }
}
