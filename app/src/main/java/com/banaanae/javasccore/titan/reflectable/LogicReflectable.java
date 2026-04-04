package com.banaanae.javasccore.titan.reflectable;

import com.banaanae.javasccore.titan.json.LogicJSONObject;
import com.banaanae.javasccore.titan.reflector.LogicJSONOutReflector;

public class LogicReflectable {
    public void destruct() {}
    
    public void save(LogicJSONObject obj) {
        final LogicJSONOutReflector out = new LogicJSONOutReflector(obj);
        
        
    }

    public int getReflectableId() {
        return 0;
    }
}
