package com.banaanae.javasccore.titan.json;

public class LogicJSONNumber extends LogicJSONNode {
    Object value;
    boolean isFloat = false;
    
    public LogicJSONNumber(int value) {
        super();
        this.value = value;
    }
    
    public LogicJSONNumber(long value) {
        super();
        this.value = value;
    }
    
    public LogicJSONNumber(float value) {
        super();
        this.value = value;
        this.isFloat = true;
    }
    
    @Override
    public void writeToString(StringBuilder sb) {
        if (!isFloat) {
            sb.append(value);
            return;
        }
        
        sb.append(String.format(java.util.Locale.US, "%.09f", (float) value));
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
