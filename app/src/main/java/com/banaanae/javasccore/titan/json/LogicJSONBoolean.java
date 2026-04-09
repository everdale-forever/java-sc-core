package com.banaanae.javasccore.titan.json;

public class LogicJSONBoolean extends LogicJSONNode {
    boolean value;
    
    public LogicJSONBoolean(boolean value) {
        super();
        this.value = value;
    }
    
    @Override
    public void writeToString(StringBuilder sb) {
        sb.append(value);
    }
    
    @Override
    public int destruct() {
        return 0;
    }
    
    @Override
    public int getType() {
        return 5;
    }
}
