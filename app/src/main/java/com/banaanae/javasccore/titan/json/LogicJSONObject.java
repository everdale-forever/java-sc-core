package com.banaanae.javasccore.titan.json;

public class LogicJSONObject extends LogicJSONNode {
    int r1;
    int r3;
    int r5;
    int r6;
    
    public LogicJSONObject() {
        super();
        // TODO: max capacity
        this.r1 = 0;
        this.r3 = 0;
        this.r5 = 0;
        this.r6 = 0;
    }
    
    @Override
    public int destruct() {
        return 0;
    }
    
    public void put(String name) {
        
    }
    
    public int getType() {
        return 2;
    }
}
