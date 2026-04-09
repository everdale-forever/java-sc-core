package com.banaanae.javasccore.titan.json;

public class LogicJSONNull extends LogicJSONNode {
    public LogicJSONNull() {}
    
    @Override
    public void writeToString(StringBuilder sb) {
        sb.append("null");
    }
    
    @Override
    public int destruct() {
        return 0;
    }
    
    @Override
    public int getType() {
        return 6;
    }
}
