package com.banaanae.javasccore.titan.json;

import com.banaanae.javasccore.titan.Debugger;

public class LogicJSONArray extends LogicJSONNode {
    LogicJSONNode[] items;
    int size;
    int capacity;
    
    public LogicJSONArray(int length) {
        super();
        this.items = null;     // a1[1]
        this.size = 0;         // a1[3]
        this.capacity = 0;     // a1[2]

        if (length >= 1) {
            this.items = new LogicJSONNode[length];
            this.capacity = length;
        }
    }
    
    public LogicJSONArray() {
        super();
        this.items = null;     // a1[1]
        this.size = 0;         // a1[3]
        this.capacity = 20;    // a1[2]
    }
    
    /**
     * Appends a LogicJSONNode to a LogicJSONArray
     * @param node Object to append
     * @return New array size
     */
    public int add(LogicJSONNode node) {
        int oldSize = this.size;
        if (oldSize == this.capacity) {
            int newCapacity = oldSize * 2;
            if (newCapacity == 0) newCapacity = 5;
            if (newCapacity < oldSize) newCapacity = Integer.MAX_VALUE;

            LogicJSONNode[] newItems = new LogicJSONNode[newCapacity];

            if (oldSize > 0 && this.items != null) {
                System.arraycopy(this.items, 0, newItems, 0, oldSize);
            }

            this.items = newItems;
            this.capacity = newCapacity;
        }

        if (this.items == null) this.items = new LogicJSONNode[this.capacity];

        this.items[oldSize] = node;
        this.size = oldSize + 1;
        return this.size;
    }
    
    /**
     * Adds a LogicJSONNode to a specific index, without overwriting
     * @param index Index to insert at
     * @param node Object to insert
     * @return New array size
     */
    public int add(int index, LogicJSONNode node) {
        int oldSize = this.size;
        if (index < 0 || index > oldSize) throw new IndexOutOfBoundsException();

        if (oldSize == this.capacity) {
            int newCapacity = oldSize * 2;
            if (newCapacity == 0) newCapacity = 5;
            if (newCapacity < oldSize) newCapacity = Integer.MAX_VALUE; // overflow guard

            LogicJSONNode[] newItems = new LogicJSONNode[newCapacity];
            if (oldSize > 0 && this.items != null) {
                System.arraycopy(this.items, 0, newItems, 0, oldSize);
            }
            this.items = newItems;
            this.capacity = newCapacity;
        }

        if (this.items == null) this.items = new LogicJSONNode[this.capacity];

        if (oldSize > index) {
            System.arraycopy(this.items, index, this.items, index + 1, oldSize - index);
        }

        this.items[index] = node;
        this.size = oldSize + 1;
        return this.size;
    }
    
    public int size() {
        return size;
    }
    
    public LogicJSONNode get(int index) {
        return items[index];
    }
    
    public int getTypeAtIndex(int index) {
        return items[index].getType();
    }
    
    public LogicJSONArray getJSONArray(int index) {
        final LogicJSONNode obj = items[index];
        final int type = obj.getType();
        
        if (type != 1) {
            final String text = String.format("LogicJSONArray::getJSONArray wrong type %d, index %d"
                    , type, index);
            Debugger.warning(text);
            return null;
        }
        
        return (LogicJSONArray) obj;
    }
    
    public LogicJSONObject getJSONObject(int index) {
        final LogicJSONNode obj = items[index];
        final int type = obj.getType();
        
        if (type != 2) {
            final String text = String.format("LogicJSONArray::getJSONObject wrong type %d, index %d"
                    , type, index);
            Debugger.warning(text);
            return null;
        }
        
        return (LogicJSONObject) obj;
    }
        
    public LogicJSONNumber getJSONNumber(int index) {
        final LogicJSONNode obj = items[index];
        final int type = obj.getType();
        
        if (type != 3) {
            final String text = String.format("LogicJSONArray::getJSONNumber wrong type %d, index %d"
                    , type, index);
            Debugger.warning(text);
            return null;
        }
        
        return (LogicJSONNumber) obj;
    }
    
    public LogicJSONString getJSONString(int index) {
        final LogicJSONNode obj = items[index];
        final int type = obj.getType();
        
        if (type != 4) {
            final String text = String.format("LogicJSONArray::getJSONString wrong type %d, index %d"
                    , type, index);
            Debugger.warning(text);
            return null;
        }
        
        return (LogicJSONString) obj;
    }

    public LogicJSONBoolean getJSONBoolean(int index) {
        final LogicJSONNode obj = items[index];
        final int type = obj.getType();
        
        if (type != 5) {
            final String text = String.format("LogicJSONArray::getJSONBoolean wrong type %d, index %d"
                    , type, index);
            Debugger.warning(text);
            return null;
        }
        
        return (LogicJSONBoolean) obj;
    }
    
    @Override
    public void writeToString(StringBuilder sb) {
        sb.append('[');
        if (size >= 1) {
            items[0].writeToString(sb);
            if (size - 1 >= 1) {
                int i = 1;
                do {
                    sb.append(',');
                    items[i].writeToString(sb);
                    i++;
                } while (i != size);
            }
        }
        sb.append(']');
    }

    @Override
    public int destruct() {
        return 0;
    }
    
    @Override
    public int getType() {
        return 1;
    }
}
