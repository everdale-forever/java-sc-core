package com.banaanae.javasccore.titan.json;

import com.banaanae.javasccore.titan.Debugger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogicJSONObject extends LogicJSONNode {
    private LogicJSONNode[] keys;
    private int keyCapacity;
    private int keyCount;
    private List<String> names;
    private int nameCapacity;
    private int nameCount;
    
    public LogicJSONObject() {
        super();
        this.names = new ArrayList<>();
        this.nameCount = 0;
        this.keyCapacity = 0;
        this.keys = new LogicJSONNode[0];
        this.keyCount = 0;
        this.keyCapacity = 0;
    }
    
    public LogicJSONObject(int initialCapacity) {
        super();
        this.keys = null;      // 4
        this.keyCapacity = 0;  // 8
        this.keyCount = 0;     // 12
        this.names = new ArrayList<>(); // 16
        this.nameCount = 0;    // 20
        this.nameCapacity = 0; // 24

        if (initialCapacity >= 1) {
            this.keys = new LogicJSONNode[initialCapacity];
            this.keyCapacity = initialCapacity;
        }

        ((ArrayList<String>) this.names).ensureCapacity(Math.max(0, initialCapacity));
        this.nameCapacity = Math.max(0, initialCapacity);
    }

    public String put(String name, LogicJSONNode value) {
        if (this.nameCount > 0) {
            for (int i = 0; i < this.nameCount; i++) {
                String existing = (String) this.names.get(i);
                if (existing != null && existing.equals(name)) {
                    Debugger.error("Duplicate key for value: " + name);
                    break;
                }
            }
        } else {
            for (int i = this.keyCount - 1; i >= 0; i--) {
                if (this.keys[i] == value) {
                    Debugger.error("Duplicate key for value: " + name);
                    break;
                }
            }
        }

        if (this.keyCount == this.keyCapacity) {
            int newCap = this.keyCapacity * 2;
            if (newCap == 0) newCap = 5;
            keys = Arrays.copyOf(keys, newCap);
            this.keyCapacity = newCap;
        }

        if (this.keyCount < keys.length) {
            keys[this.keyCount] = value;
        } else {
            keys = Arrays.copyOf(keys, this.keyCount + 1);
            keys[this.keyCount] = value;
            this.keyCapacity = keys.length;
        }
        this.keyCount++;

        if (this.nameCount == this.nameCapacity) {
            int newCap = this.nameCapacity * 2;
            if (newCap == 0) newCap = 5;
            ((ArrayList<String>) this.names).ensureCapacity(newCap);
            this.nameCapacity = newCap;
        }

        if (this.nameCount < this.names.size()) {
            this.names.set(this.nameCount, name);
        } else {
            this.names.add(name);
        }
        this.nameCount++;

        return name;
    }
    
    @Override
    public void writeToString(StringBuilder sb) {
        sb.append('{');
        if (nameCount > 0) {
            for (int i = 0; i != nameCount; ++i) {
                String value = names.get(i);
                if (i != 0)
                    sb.append(',');
                LogicJSONParser.writeString(value, sb);
                sb.append(':');
                keys[i].writeToString(sb);
            }
        }
        sb.append('}');
    }

    
    @Override
    public int destruct() {
        return 0;
    }
    
    public void put(String name) {
        
    }
    
    @Override
    public int getType() {
        return 2;
    }
}
