package com.banaanae.javasccore.logic.data;

import com.banaanae.javasccore.titan.Debugger;
import com.banaanae.javasccore.titan.csv.CSVNode;
import com.banaanae.javasccore.titan.csv.CSVTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogicDataTables {
    private static final Map<Integer, LogicDataTable> tables = new HashMap<>();

    public static void initDataTables(CSVNode node, int index) {
        final CSVTable table = node.getTable();

        if (table == null)
            Debugger.error("Unable to find table from", node.getFileName());

        if (LogicDataTables.tables.containsKey(index)) {
            LogicDataTables.tables.get(index).setTable(table);
        } else {
            // TODO: globals

            LogicDataTables.tables.put(index, new LogicDataTable(table, index));
        }
    }

    public static void createReferences() {
        for (int i = 0; i < tables.size(); i++) {
            if (LogicDataTables.tables.get(i) != null)
                LogicDataTables.tables.get(i).createReferences();
        }

        for (int i = 0; i < tables.size(); i++) {
            if (LogicDataTables.tables.get(i) != null)
                LogicDataTables.tables.get(i).createReferences2();
        }
    }

    public static LogicData getDataByName(String name, int tableId) {
        if (LogicDataTables.tables.get(tableId) != null)
            return LogicDataTables.tables.get(tableId).getDataByName(name);

        return null;
    }

    public static LogicDataTable getTable(int tableIdx) {
        return tables.get(tableIdx);
    }
}
