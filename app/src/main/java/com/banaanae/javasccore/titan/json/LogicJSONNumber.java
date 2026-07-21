package com.banaanae.javasccore.titan.json;

import com.banaanae.javasccore.titan.LogicLong;

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

    public LogicJSONNumber(LogicLong value) {
        super();
        this.value = value.toLong();
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
