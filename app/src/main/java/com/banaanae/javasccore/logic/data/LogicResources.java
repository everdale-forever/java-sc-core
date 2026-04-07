package com.banaanae.javasccore.logic.data;

import com.banaanae.javasccore.titan.Debugger;
import com.banaanae.javasccore.titan.csv.CSVNode;
import java.util.ArrayList;
import java.util.List;

public class LogicResources {
    public static List<LogicDataTableResource> createDataTableResourcesArray() {
        final List<LogicDataTableResource> DataTables = new ArrayList<>();
        
        // Load your csv here...
        // DataTables.add(new LogicDataTableResource("PATH", LogicDataType.Idx, Type));
        
        return DataTables;
    }
    
    public static void load(List<LogicDataTableResource> resources, int id, CSVNode node) {
        final LogicDataTableResource resource = resources.get(id);
        switch (resource.getTableType()) {
            case 0 -> LogicDataTables.initDataTables(node, resource.getTableIndex());
            case 3 -> { /* StringTable */ }
            default -> Debugger.error("LogicResources::Invalid resource type");
        }
        if (resources.size() - 1 == id)
            LogicDataTables.createReferences();
    }
}
