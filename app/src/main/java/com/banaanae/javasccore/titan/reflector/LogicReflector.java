package com.banaanae.javasccore.titan.reflector;

import com.banaanae.javasccore.titan.Debugger;
import com.banaanae.javasccore.titan.GlobalID;
import com.banaanae.javasccore.titan.LogicCompressedString;
import com.banaanae.javasccore.titan.LogicLong;
import com.banaanae.javasccore.titan.json.LogicJSONNode;
import com.banaanae.javasccore.titan.random.LogicRandom;
import com.banaanae.javasccore.titan.reflectable.LogicReflectable;
import com.banaanae.javasccore.titan.reflectable.LogicReflectableReferenceBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class LogicReflector {
    public static String SCHEMA_VERSION = "";
    public static String DEFAULT_STRING = "";

    public static HashMap<Integer, LogicReflectable> REFLECTABLE_ID_MAP = new HashMap<>(Map.of(

    ));

    public abstract void destruct();
    
    public void checkReflectableIdRequiredType(int id, int reqType) {
        System.out.println(GlobalID.getClassId(id) + " " + id + " " + reqType);
        if (GlobalID.getClassId(id) != reqType && reqType != -1)
            Debugger.error("checkReflectableIdRequiredType: required type mismatch");
    }
    
    public void checkReflectableIdArrayRequiredType(ArrayList<Integer> arr, int reqType) {
        if (reqType != -1 && !arr.isEmpty()) {
            for (int id : arr)
                if (id >= 1 && GlobalID.getClassId(id) != reqType)
                    Debugger.error("checkReflectableIdArrayRequiredType: required type mismatch");
        }
    }

    public abstract boolean reflectObject(String objectName);
    public abstract boolean reflectObjectOptional(String objectName, boolean hasObj);
    public abstract void reflectExitObject();
    public abstract int reflectInt(int value, String keyName, int defaultVal);
    public abstract boolean reflectBool(boolean value, String objectName, boolean defaultVal);
    public abstract long reflectLong(long value, String objectName, long defaultVal);
    public abstract float reflectFloat(float value, String objectName, float defaultVal);
    public abstract String reflectString(String value, String objectName, String defaultVal);
    public abstract String reflectStringPtr(String value, String objectName);
    public abstract byte[] reflectCompressedString(LogicCompressedString value, String objectName);
    public abstract void reflectStringBuilder(StringBuilder value, String objectName);
    public abstract LogicJSONNode reflectJSONPtr(LogicJSONNode value, String objectName);
    public abstract void reflectRandom(LogicRandom value, String objectName);
    public abstract ArrayList<Integer> reflectIntArray(ArrayList<Integer> arr, String objectName);
    public abstract ArrayList<Long> reflectLongArray(ArrayList<Long> arr, String objectName);
    public abstract ArrayList<LogicLong> reflectLogicLongArray(ArrayList<LogicLong> arr, String objectName);
    public abstract byte[] reflectSimpleByteArray(byte[] values, int length, String objectName);
    public abstract int[] reflectSimpleIntArray(int[] values, int length, String objectName);
    public abstract long[] reflectSimpleLongArray(long[] values, int length, String objectName);
    public abstract LogicReflectable reflectReflectablePointerBase(LogicReflectable data, String objectName, int reqType);
    public abstract int reflectArray(int length, String objectName);
    public abstract void reflectExitArray();
    public abstract boolean reflectNextObject();
    public abstract boolean reflectNextObjectOptional(boolean hasObj);
    public abstract int reflectNextInt(int value);
    public abstract boolean reflectNextBool(boolean value);
    public abstract long reflectNextLong(long value);
    public abstract float reflectNextFloat(float value);
    public abstract String reflectNextString(String value);
    public abstract LogicReflectable reflectNextReflectablePointer(LogicReflectable reflectable, int reqType);
    public abstract void reflectNextReflectableReferenceBase(LogicReflectableReferenceBase value, int reqType);
    public abstract LogicReflectable reflectNextReflectable(LogicReflectable reflectable, int reqType);
    public abstract void reflectReflectableReferenceBase(LogicReflectableReferenceBase value, String objectName, int reqType);
    public abstract void fixReferences();
    public abstract void reflectReflectableReferenceArrayInternal(ArrayList<Integer> data, ArrayList<LogicReflectable> reflectables, String objectName, int reqType);
}
