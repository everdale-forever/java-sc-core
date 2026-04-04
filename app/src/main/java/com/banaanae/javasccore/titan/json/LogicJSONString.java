package com.banaanae.javasccore.titan.json;

public class LogicJSONString extends LogicJSONNode {
    String value;
    
    public LogicJSONString(String value) {
        this.value = value;
    }
    
    @Override
    public int destruct() {
        return 0;
    }
    
    @Override
    public int getType() {
        return 4;
    }
}
