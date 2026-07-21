package com.banaanae.javasccore.titan.reflector;

import com.banaanae.javasccore.titan.Debugger;
import com.banaanae.javasccore.titan.LogicCompressedString;
import com.banaanae.javasccore.titan.LogicLong;
import com.banaanae.javasccore.titan.datastream.bytestream.ByteStream;
import com.banaanae.javasccore.titan.json.LogicJSONNode;
import com.banaanae.javasccore.titan.json.LogicJSONParser;
import com.banaanae.javasccore.titan.random.LogicRandom;
import com.banaanae.javasccore.titan.reflectable.LogicReflectable;
import com.banaanae.javasccore.titan.reflectable.LogicReflectableReferenceBase;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.util.ArrayList;

public class LogicRawInReflector extends LogicReflector {
    int unk24;
    int unk40;
    int unk56;
    int unk72;

    ByteStream stream;

    public LogicRawInReflector(ByteStream stream) {
        this.unk24 = 0;
        this.unk40 = 0;
        this.unk56 = 0;
        this.unk72 = 0;
        this.stream = stream;
    }

    public void destruct() {
        stream = null;
    }

    public boolean reflectObject(String objectName) {
        return true;
    }

    public boolean reflectObjectOptional(String objName, boolean hasObj) {
        hasObj = stream.readBoolean();
        if (hasObj)
            reflectObject(null);
        return hasObj;
    }

    public void reflectExitObject() {
        // Everdale does nothing
    }

    public int reflectInt(int value, String keyName, int defaultVal) {
        boolean hasVal = stream.readBoolean();
        if (hasVal) {
            return stream.readVInt();
        } else {
            return defaultVal;
        }
    }

    public boolean reflectBool(boolean value, String objectName, boolean defaultVal) {
        return stream.readBoolean();
    }

    @Override
    public long reflectLong(long value, String objectName, long defaultVal) {
        boolean hasVal = stream.readBoolean();
        if (hasVal) {
            return stream.readVLong();
        }
        return defaultVal;
    }

    public float reflectFloat(float value, String objectName, float defaultVal) {
        boolean hasVal = stream.readBoolean();
        if (hasVal) {
            defaultVal = Float.intBitsToFloat(stream.readInt());
        }
        return defaultVal;
    }

    public String reflectString(String value, String objectName, String defaultVal) {
        boolean hasVal = stream.readBoolean();
        if (hasVal) {
            return stream.readStringReference();
        }
        return defaultVal;
    }

    public String reflectStringPtr(String value, String objectName) {
        return stream.readString();
    }

    public byte[] reflectCompressedString(LogicCompressedString value, String objectName) {
        value.clear();
        return value.decodeRef(stream);
    }

    public void reflectStringBuilder(StringBuilder stringBuilder, String objectName) {
        final String str = stream.readStringReference();
        stringBuilder.setLength(0);
        stringBuilder.append(str);
    }

    public LogicJSONNode reflectJSONPtr(LogicJSONNode value, String objectName) {
        boolean hasVal = stream.readBoolean();
        System.out.println("TODO: reflectJSONPtr");
        if (false) {
            final LogicCompressedString json = new LogicCompressedString();
            json.decode(stream);
            final String jsonStr = json.getString();

            return LogicJSONParser.parseJSONNode(jsonStr, 0);
        } else {
            return null;
        }
    }

    public void reflectRandom(LogicRandom random, String objectName) {
        int newSeed = stream.readInt();
        random.setIteratedSeed(newSeed);
    }

    public ArrayList<Integer> reflectIntArray(ArrayList<Integer> arr, String objectName) {
        final int len = stream.readInt();

        for (int i = 0; i < len; i++) {
            arr.add(stream.readInt());
        }

        return arr;
    }

    public ArrayList<Long> reflectLongArray(ArrayList<Long> arr, String objectName) {
        final int len = stream.readInt();

        for (int i = 0; i < len; i++) {
            arr.add(stream.readLongLong());
        }

        return arr;
    }

    public ArrayList<LogicLong> reflectLogicLongArray(ArrayList<LogicLong> arr, String objectName) {
        final int len = stream.readInt();

        for (int i = 0; i < len; i++) {
            arr.add(new LogicLong(stream.readLongLong()));
        }

        return arr;
    }

    public byte[] reflectSimpleByteArray(byte[] values, int length, String objectName) {
        final ArrayList<Byte> result = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            result.add(stream.readByte());
        }

        return Bytes.toArray(result);
    }

    public int[] reflectSimpleIntArray(int[] values, int length, String objectName) {
        final ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            result.add(stream.readInt());
        }

        return Ints.toArray(result);
    }

    public long[] reflectSimpleLongArray(long[] values, int length, String objectName) {
        final ArrayList<Long> result = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            result.add(stream.readVLong());
        }

        return Longs.toArray(result);
    }

    public LogicReflectable reflectReflectablePointerBase(LogicReflectable data, String objectName, int reqType) {
        final int type = stream.readVInt();
        if (type == 0)
            return null;

        return null;
    }

    public int reflectArray(int length, String objectName) {
        length = stream.readVInt();
        if ((length + 1) >>> 24 != 0)
            Debugger.error("Too large array");

        return length;
    }

    public void reflectExitArray() {

    }

    @Override
    public boolean reflectNextObject() {
        return true;
    }

    @Override
    public boolean reflectNextObjectOptional(boolean hasObj) {
        hasObj = stream.readBoolean();
        if (hasObj)
            reflectNextObject();
        return hasObj;
    }

    @Override
    public int reflectNextInt(int value) {
        return stream.readVInt();
    }

    @Override
    public boolean reflectNextBool(boolean value) {
        return stream.readBoolean();
    }

    @Override
    public long reflectNextLong(long value) {
        return stream.readLongLong();
    }

    @Override
    public float reflectNextFloat(float value) {
        System.out.println("TODO: reflectNextFloat");
        return 0;
    }

    @Override
    public String reflectNextString(String value) {
        return stream.readStringReference();
    }

    @Override
    public LogicReflectable reflectNextReflectablePointer(LogicReflectable reflectable, int reqType) {
        System.out.println("TODO: reflectNextReflectablePointer");
        return null;
    }

    @Override
    public void reflectNextReflectableReferenceBase(LogicReflectableReferenceBase value, int reqType) {
        System.out.println("TODO: reflectNextReflectableReferenceBase");
    }

    @Override
    public LogicReflectable reflectNextReflectable(LogicReflectable reflectable, int reqType) {
        System.out.println("TODO: reflectNextReflectable");
        return null;
    }

    @Override
    public void reflectReflectableReferenceBase(LogicReflectableReferenceBase value, String objectName, int reqType) {
        System.out.println("TODO: reflectReflectableReferenceBase");
    }

    @Override
    public void fixReferences() {
        System.out.println("TODO: fixReferences");
    }

    @Override
    public void reflectReflectableReferenceArrayInternal(ArrayList<Integer> data, ArrayList<LogicReflectable> reflectables, String objectName, int reqType) {
        System.out.println("TODO: reflectReflectableReferenceArrayInternal");
    }
}
