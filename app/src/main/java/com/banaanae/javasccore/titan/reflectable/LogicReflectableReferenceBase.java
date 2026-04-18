package com.banaanae.javasccore.titan.reflectable;

public class LogicReflectableReferenceBase {
    private int id;
    
    public LogicReflectableReferenceBase() {
        id = 0;
    }
    
    public LogicReflectableReferenceBase(int id) {
        this.id = id;
    }
    
    public LogicReflectableReferenceBase(LogicReflectable reflectable) {
        this.id = -1;
        if (reflectable != null) {
            // TODO
        }
    }
    
    public void setId(int newId) {
        this.id = newId;
    }
    
    public int getId() {
        return id;
    }
}
