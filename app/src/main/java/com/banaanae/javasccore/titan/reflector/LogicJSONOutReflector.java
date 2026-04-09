package com.banaanae.javasccore.titan.reflector;

import com.banaanae.javasccore.titan.Debugger;
import com.banaanae.javasccore.titan.LogicCompressedString;
import com.banaanae.javasccore.titan.LogicLong;
import com.banaanae.javasccore.titan.json.*;
import com.banaanae.javasccore.titan.random.LogicRandom;
import com.banaanae.javasccore.titan.reflectable.LogicReflectable;
import com.banaanae.javasccore.titan.reflectable.LogicReflectableReferenceBase;

public class LogicJSONOutReflector extends LogicReflector {
    private LogicJSONNode[] stackNodes;   // 8
    private int stackCapacity;            // 16
    private int stackSize;                // 20
    private String[] values;              // 24
    private int valuesCapacity;           // 32
    private int valuesSize;               // 36
    public LogicJSONObject currentObject; // 40
    public LogicJSONArray currentArray;   // 48
    private String unk;                   // 64
    
    public LogicJSONOutReflector(LogicJSONNode data) {
        final int type = data.getType();
        
        this.stackNodes = null;
        this.values = null;
        this.currentObject = null;
        
        switch (type) {
            case 2:
                this.currentObject = (LogicJSONObject) data;
                break;
            case 1:
                this.currentArray = (LogicJSONArray) data;
                break;
            default:
                Debugger.error("LogicJSONOutReflector - Unsupported root object type");
                break;
        }
    }
    
    @Override
    public void destruct() {
        if (stackSize > 0)
            Debugger.error("Mismatched begin/end or enter/exits in LogicJSONOutReflector");
        
        stackSize = 0;
        valuesSize = 0;
    }
    
    public boolean reflectObject(String objectName) {
        beginObject(objectName);
        return true;
    }
    
    public boolean reflectObjectOptional(String objectName, boolean a3) {
        if (a3) // TODO
            beginObject(objectName);
        return a3;
    }
    
    public void reflectExitObject() {
        endObject();
    }
    
    public int reflectInt(int value, String objectName, int defaultVal) {
        if (currentObject == null) {
            Debugger.error("LogicJSONOutReflector: no object exists");
        }
        if (value != defaultVal) {
            final LogicJSONNumber obj = new LogicJSONNumber(value);
            currentObject.put(objectName, obj);
        }
        return value;
    }
    
    public boolean reflectBool(boolean value, String objectName, boolean defaultVal) {
        if (currentObject == null) {
            Debugger.error("LogicJSONOutReflector: no object exists");
        }
        if (value != defaultVal) {
            final LogicJSONBoolean obj = new LogicJSONBoolean(value);
            currentObject.put(objectName, obj);
        }
        return value;
    }
    
    public long reflectLong(long value, String objectName, long defaultVal) {
        if (currentObject == null) {
            Debugger.error("LogicJSONOutReflector: no object exists");
        }
        if (value != defaultVal) {
            final LogicJSONNumber obj = new LogicJSONNumber(value);
            currentObject.put(objectName, obj);
        }
        return value;
    }
    
    public float reflectFloat(float value, String objectName, float defaultVal) {
        if (currentObject == null) {
            Debugger.error("LogicJSONOutReflector: no object exists");
        }
        if (value != defaultVal) {
            final LogicJSONNumber obj = new LogicJSONNumber(value);
            currentObject.put(objectName, obj);
        }
        return value;
    }
    
    public String reflectString(String value, String objectName, String defaultVal) {
        setString(objectName, value, defaultVal);
        return value;
    }
    
    public String reflectStringPtr(String value, String objectName) {
        if (currentObject == null) {
            Debugger.error("LogicJSONOutReflector: no object exists");
        }
        
        if (value != null) {
            final LogicJSONString obj = new LogicJSONString(value);
            currentObject.put(objectName, obj);
        }
        
        return value;
    }
    
    public void reflectCompressedString(LogicCompressedString value, String objectName) {
        System.out.println("TODO: reflectCompressedString");
    }
    
    // reflectStringBuilder
    // reflectJSONPtr
    
    public void reflectRandom(LogicRandom value, String objectName) {
        final int seed = value.getIteratedSeed();
        
        if (seed == 0) {
            return;
        } else if (currentObject == null) {
            Debugger.error("LogicJSONOutReflector: no object exists");
        }
        
        final LogicJSONNumber obj = new LogicJSONNumber(seed);
        currentObject.put(objectName, obj);
    }
    
    public void reflectIntArray(int[] arr, String objectName) {
        final int length = arr.length;
        
        if (length >= 1) {
            beginArray(objectName, length);
            for (int i = 0; i != length; ++i) {
                final LogicJSONNumber obj = new LogicJSONNumber(arr[i]);
                currentArray.add(obj);
            }
            endArray();
        }
    }
    
    public void reflectLongArray(long[] arr, String objectName) {
        final int length = arr.length;
        
        if (length >= 1) {
            beginArray(objectName, length);
            for (int i = 0; i != length; ++i) {
                final LogicJSONNumber obj = new LogicJSONNumber(arr[i]);
                currentArray.add(obj);
            }
            endArray();
        }
    }
    
    public void reflectLongArray(LogicLong[] arr, String objectName) {
        final int length = arr.length;
        
        if (length >= 1) {
            beginArray(objectName, length);
            for (int i = 0; i != length; ++i) {
                final LogicJSONNumber obj = new LogicJSONNumber(arr[i].toLong());
                currentArray.add(obj);
            }
            endArray();
        }
    }

    // reflectSimpleByteArray
    // reflectSimpleIntArray
    // reflectSimpleLongArray
    
    public LogicReflectable reflectReflectablePointerBase(LogicReflectable reflectable, String objectName) {
        if (reflectable != null) {
            final int reflectableId = reflectable.getReflectableId();
            if (reflectableId == 0)
                Debugger.error("LogicJSONOutReflector::reflectReflectablePointerBase() - reflectable id is zero, is missing from a reflectable id map?");
            
            if (currentObject == null)
                Debugger.error("LogicJSONOutReflector: no object exists");
            
            final LogicJSONNumber obj = new LogicJSONNumber(reflectableId);
            currentObject.put(objectName, obj);
        }
        return reflectable;
    }
    
    public int reflectArray(int length, String objectName) {
        if (length < 1)
            return -1;
        
        beginArray(objectName, length);
        return length;
    }
    
    public void reflectExitArray() {
        endArray();
    }
    
    public boolean reflectNextObject() {
        if (currentArray == null)
            Debugger.warning("LogicJSONOutReflector: no current array exists");
        
        beginObject(null);
        return true;
    }
    
    public boolean reflectNextObjectOptional(boolean a2) {
        if (a2) {
            if (currentArray == null)
                Debugger.error("LogicJSONOutReflector: no current array exists"); 
            
            beginObject(null);
        } else {
            final LogicJSONNull obj = new LogicJSONNull();
            currentArray.add(obj);
        }

        return a2;        
    }
    
    public int reflectNextInt(int value) {
        if (currentArray == null)
            Debugger.warning("LogicJSONOutReflector: no current array exists");
        
        final LogicJSONNumber obj = new LogicJSONNumber(value);
        currentArray.add(obj);
        return value;
    }
    
    public boolean reflectNextBool(boolean value) {
        if (currentArray == null)
            Debugger.error("LogicJSONOutReflector: no object exists");
        
        final LogicJSONBoolean obj = new LogicJSONBoolean(value);
        currentArray.add(obj);
        return value;
    }
        
    public long reflectNextLong(long value) {
        if (currentArray == null)
            Debugger.warning("LogicJSONOutReflector: no current array exists");
        
        final LogicJSONNumber obj = new LogicJSONNumber(value);
        currentArray.add(obj);
        return value;
    }
            
    public float reflectNextFloat(float value) {
        if (currentArray == null)
            Debugger.warning("LogicJSONOutReflector: no current array exists");
        
        final LogicJSONNumber obj = new LogicJSONNumber(value);
        currentArray.add(obj);
        return value;
    }
    
    public String reflectNextString(String value) {
        if (currentArray == null)
            Debugger.warning("LogicJSONOutReflector: no current array exists");
        
        final LogicJSONString obj = new LogicJSONString(value);
        currentArray.add(obj);
        return value;
    }
    
    public LogicReflectable reflectNextReflectablePointer(LogicReflectable reflectable, String objectName, int reqType) {
        int id;
        if (reflectable != null) {
            id = reflectable.getReflectableId();
            checkReflectableIdRequiredType(id, reqType);
        } else {
            id = 0;
        }
        
        if (currentArray == null)
            Debugger.error("LogicJSONOutReflector: no current array exists");
        
        final LogicJSONNumber obj = new LogicJSONNumber(id);
        currentArray.add(obj);
        
        return reflectable;
    }
    
    public void reflectNextReflectableReferenceBase(LogicReflectableReferenceBase value, String objectName, int reqType) {
        final int id = value.getId();
        
        if (currentArray == null)
            Debugger.error("LogicJSONOutReflector: no current array exists");
        
        final LogicJSONNumber obj = new LogicJSONNumber(id);
        currentArray.add(obj);
    }
    
    public LogicReflectable reflectNextReflectable(LogicReflectable reflectable, int reqType) {
        if (reflectable != null) {
            if (currentArray == null)
                Debugger.error("LogicJSONOutReflector: no current array exists");
            
            beginObject(null);
            final int id = reflectable.getReflectableId();
            if (reqType == -1 && id != 0) {
                final LogicJSONNumber typeObj = new LogicJSONNumber(id);
                currentObject.put("objectType", typeObj);
            } else if (id != reqType) {
                Debugger.error("reflectNextReflectable - value type doesn't match required type");
            }
            // LogicReflectable::getReflectableDataSchemaVersion
            endObject();
        } else {
            final LogicJSONNull obj = new LogicJSONNull();
            currentArray.add(obj);
        }
        return reflectable;
    }
    
    public void reflectReflectableReferenceBase(LogicReflectableReferenceBase value, String objectName, int reqType) {
        final int id = value.getId();
        checkReflectableIdRequiredType(id, reqType);
        
        if (currentObject != null) {
            if (id == -1)
                return;
        } else {
            Debugger.error("LogicJSONOutReflector: no object exists");
        }
        
        final LogicJSONNumber obj = new LogicJSONNumber(id);
        currentObject.put(objectName, obj);
    }
    
    public void fixReferences() {}
    
    // reflectReflectableReferenceArrayInternal(int id, int64 a3 (unused), String objectName, int reqType)
    
    
    private void setString(String objectName, String value, String defaultVal) {
        if (currentObject == null)
            Debugger.error("LogicJSONOutReflector: no object exists");
        if (!value.equals(defaultVal)) {
            LogicJSONString obj = new LogicJSONString(value);
            currentObject.put(objectName, obj);
        }
    }
    
    private void beginObject(String objectName) {
        final LogicJSONObject obj = new LogicJSONObject();
        if (currentObject != null) {
            currentObject.put(objectName, obj);
        } else {
            currentArray.add(obj);
        }
        pushStack();
        currentObject = obj;
    }
    
    private void endObject() {
        if (currentObject == null) {
            Debugger.error("endObject called while no current object exists");
        } else if (stackSize <= 0) {
            Debugger.error("Mismatched begin/end or enter/exits in LogicJSONOutReflector");
        }
        this.stackSize--;
        this.stackCapacity--;
        LogicJSONNode parent = this.stackNodes[stackSize];
        this.currentObject = null;
        int rootType = parent.getType();
        
        switch (rootType) {
            case 2:
                this.currentObject = (LogicJSONObject) parent;
                break;
            case 1:
                this.currentArray = (LogicJSONArray) parent;
                break;
            default:
                Debugger.error("LogicJSONOutReflector - Unsupported object type in stack");
                break;
        }
    }
    
    private void beginArray(String objectName, int length) {
        final LogicJSONArray arr = new LogicJSONArray(length);
        
        if (currentObject != null) {
            currentObject.put(objectName, arr);
        } else {
            currentArray.add(arr);
        }
        
        pushStack();
        currentArray = arr;
    }
    
    private void endArray() {
        if (currentArray == null)
            Debugger.error("endArray called while no current array exists");
        if (stackSize <= 0 || valuesSize <= 0)
            Debugger.error("Mismatched begin/end or enter/exits in LogicJSONOutReflector");
        
        this.valuesSize--;
        this.stackSize--;
        LogicJSONNode parent = stackNodes[stackSize];
        currentObject = null;
        this.unk = values[valuesSize];
        currentArray = null;
        
        final int type = parent.getType();
        switch (type) {
            case 2:
                this.currentObject = (LogicJSONObject) parent;
                break;
            case 1:
                this.currentArray = (LogicJSONArray) parent;
                break;
            default:
                Debugger.error("LogicJSONOutReflector - Unsupported object type in stack");
                break;
        }
    }
    
    public void pushStack() {
        LogicJSONNode toPush = (currentObject != null) ? currentObject : currentArray;

        if (stackNodes == null || stackSize == stackCapacity) {
            int oldCap = stackCapacity;
            int newCap = (oldCap != 0) ? oldCap * 2 : 5;
            if (oldCap < newCap) {
                LogicJSONNode[] newArr = new LogicJSONNode[newCap];
                if (stackNodes != null && stackSize > 0) {
                    System.arraycopy(stackNodes, 0, newArr, 0, stackSize);
                }
                this.stackNodes = newArr;
                this.stackCapacity = newCap;
            }
        }

        stackNodes[this.stackSize++] = toPush;

        if (values == null || valuesSize == valuesCapacity) {
            int oldCap = valuesCapacity;
            int newCap = (oldCap != 0) ? oldCap * 2 : 5;
            if (oldCap < newCap) {
                String[] newVals = new String[newCap];
                if (values != null && valuesSize > 0) {
                    System.arraycopy(values, 0, newVals, 0, valuesSize);
                }
                this.values = newVals;
                this.valuesCapacity = newCap;
            }
        }

        values[this.valuesSize++] = unk;
        unk = null;

        currentObject = null;
        currentArray = null;
    }
}
