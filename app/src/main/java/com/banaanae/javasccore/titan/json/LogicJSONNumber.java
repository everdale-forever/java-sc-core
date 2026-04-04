package com.banaanae.javasccore.titan.json;

public class LogicJSONNumber extends LogicJSONNode {
    int value;
    
    public LogicJSONNumber(int value) {
        super();
        this.value = value;
    }
    
    public LogicJSONNumber(long value) {
        
    }
    
    public LogicJSONNumber(float value) {
        
    }
    
    @Override
    public int destruct() {
        return 0;
    }
    
    @Override
    public int getType() {
        return 3;
    }
}
