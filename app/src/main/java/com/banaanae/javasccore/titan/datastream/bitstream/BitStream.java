package com.banaanae.javasccore.titan.datastream.bitstream;

import com.banaanae.javasccore.titan.Debugger;
import com.banaanae.javasccore.titan.LogicMath;

public class BitStream {
    byte[] stream; // 16
    int offset = 0; // 4
    int bitOffset = 0; // 12
    int capacity = 0; // 20
    
    public BitStream(byte[] bytes) {
        this.stream = bytes;
    }
    
    public BitStream(int capacity) {
        this.stream = new byte[capacity];
        this.capacity = capacity;
    }

    public void destruct() {
        this.stream = null;
        this.offset = 0;
        this.bitOffset = 0;
    }
    
    private void ensureCapacity() {
        if (offset + 6 > capacity) {
            int newCapacity = capacity + 105;

            byte[] newStream = new byte[newCapacity];

            if (stream != null)
                System.arraycopy(stream, 0, newStream, 0, offset + 1);

            this.stream = newStream;
            this.capacity = newCapacity;
        }
    }
    
    public byte[] getByteArray() {
        return stream;
    }
    
    public int getCapacityIncrement() {
        return 100;
    }
    
    public int getLength() {
        return offset;
    }
    
    public void resetOffset() {
        this.offset = 0;
        this.bitOffset = 0;
    }

    public int readOneBit() {
        byte currByte = stream[offset];
        int bit = (currByte & (1 << bitOffset)) >> bitOffset;

        if (bitOffset == 7) {
            this.offset++;
            this.bitOffset = 0;
        } else {
            this.bitOffset++;
        }

        return bit;
    }

    public int readBits(int count) {
        if (count <= 0) {
            return 0;
        }

        int result = 0;

        for (int i = 0; i < count; i++) {
            result |= readOneBit() << i;
        }

        return result;
    }
    
    public boolean readBoolean() {
        return readOneBit() == 1;
    }
    
    public int readIntMax1() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(1);

        return sign * magnitude;
    }
    
    public int readIntMax3() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(2);

        return sign * magnitude;
    }
    
    public int readIntMax7() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(3);

        return sign * magnitude;
    }
    
    public int readIntMax15() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(4);

        return sign * magnitude;
    }
    
    public int readIntMax31() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(5);

        return sign * magnitude;
    }
    
    public int readIntMax63() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(6);

        return sign * magnitude;
    }
    
    public int readIntMax127() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(7);

        return sign * magnitude;
    }
    
    public int readIntMax255() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(8);

        return sign * magnitude;
    }
    
    public int readIntMax511() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(9);

        return sign * magnitude;
    }
    
    public int readIntMax1023() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(10);

        return sign * magnitude;
    }
    
    public int readIntMax2047() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(11);

        return sign * magnitude;
    }
    
    public int readIntMax4095() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(12);

        return sign * magnitude;
    }
    
    public int readIntMax8191() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(13);

        return sign * magnitude;
    }
    
    public int readIntMax16383() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(14);

        return sign * magnitude;
    }
    
    public int readIntMax32767() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(15);

        return sign * magnitude;
    }
    
    public int readIntMax65535() {
        int sign = readOneBit() * -1;
        int magnitude = readBits(16);

        return sign * magnitude;
    }

    public void writeOneBit(int value) {
        if (offset == 0 && bitOffset == 0) {
            stream[0] = 0;
        }

        stream[offset] |= (byte) (value << bitOffset);

        bitOffset++;
        if (bitOffset == 8) {
            bitOffset = 0;
            offset++;

            if (offset < stream.length) {
                stream[offset] = 0;
            }
        }
    }

    public void writeBits(int value, int count) {
        for (int i = 0; i < count; i++) {
            writeOneBit((value >> i) & 1);
        }
    }
    
    public void writeBoolean(boolean value) {
        writePositiveInt(value ? 1 : 0, 1);
    }
    
    public void writeIntMax1(int value) {
        writeInt(value, 1);
    }
    
    public void writeIntMax3(int value) {
        writeInt(value, 2);
    }
    
    public void writeIntMax7(int value) {
        writeInt(value, 3);
    }
    
    public void writeIntMax15(int value) {
        writeInt(value, 4);
    }
    
    public void writeIntMax31(int value) {
        writeInt(value, 5);
    }
    
    public void writeIntMax63(int value) {
        writeInt(value, 6);
    }

    public void writeIntMax127(int value) {
        writeInt(value, 7);
    }
    
    public void writeIntMax255(int value) {
        writeInt(value, 8);
    }
    
    public void writeIntMax511(int value) {
        writeInt(value, 9);
    }
    
    public void writeIntMax1023(int value) {
        writeInt(value, 10);
    }
    
    public void writeIntMax2047(int value) {
        writeInt(value, 11);
    }
    
    public void writeIntMax4095(int value) {
        writeInt(value, 12);
    }
    
    public void writeIntMax8191(int value) {
        writeInt(value, 13);
    }
    
    public void writeIntMax16383(int value) {
        writeInt(value, 14);
    }
    
    public void writeIntMax32767(int value) {
        writeInt(value, 15);
    }
    
    public void writeIntMax65535(int value) {
        writeInt(value, 16);
    }

    public void writeInt(int value, int bitsCount) {
        if (value != LogicMath.clamp(value,
                (-1 << bitsCount) + 1,
                ~(-1 << bitsCount))) {
            Debugger.error(String.format(
                    "Write to BitStream out of range! (integer: %d, bits: %d)",
                    value, bitsCount));
        }

        ensureCapacity();

        writeOneBit(value >= 0 ? 1 : 0);
        writeBits(LogicMath.abs(value), bitsCount);
    }
    
    public int readPositiveIntMax1() {
        return readBits(1);
    }
    
    public int readPositiveIntMax3() {
        return readBits(2);
    }
    
    public int readPositiveIntMax7() {
        return readBits(3);
    }
    
    public int readPositiveIntMax15() {
        return readBits(4);
    }
    
    public int readPositiveIntMax31() {
        return readBits(5);
    }
    
    public int readPositiveIntMax63() {
        return readBits(6);
    }
    
    public int readPositiveIntMax127() {
        return readBits(7);
    }
    
    public int readPositiveIntMax255() {
        return readBits(8);
    }
    
    public int readPositiveIntMax511() {
        return readBits(9);
    }
    
    public int readPositiveIntMax1023() {
        return readBits(10);
    }
    
    public int readPositiveIntMax2047() {
        return readBits(11);
    }
    
    public int readPositiveIntMax4095() {
        return readBits(12);
    }
    
    public int readPositiveIntMax8191() {
        return readBits(13);
    }
    
    public int readPositiveIntMax16383() {
        return readBits(14);
    }
    
    public int readPositiveIntMax32767() {
        return readBits(15);
    }
    
    public int readPositiveIntMax65535() {
        return readBits(16);
    }
    
    public int readPositiveIntMax131071() {
        return readBits(17);
    }
    
    public int readPositiveIntMax262143() {
        return readBits(18);
    }
    
    public int readPositiveIntMax524287() {
        return readBits(19);
    }
    
    public int readPositiveIntMax1048575() {
        return readBits(20);
    }
    
    public int readPositiveIntMax2097151() {
        return readBits(21);
    }
    
    public int readPositiveIntMax4194303() {
        return readBits(22);
    }

    public int readPositiveIntMax8388608() {
        return readBits(23);
    }
    
    public int readPositiveIntMax16777215() {
        return readBits(24);
    }
    
    public int readPositiveIntMax33554431() {
        return readBits(25);
    }
    
    public int readPositiveIntMax67108863() {
        return readBits(26);
    }
    
    public int readPositiveIntMax134217727() {
        return readBits(27);
    }

    public void writePositiveInt(int value, int count) {
        if (value != LogicMath.clamp(value, 0, ~(-1 << count))) {
            Debugger.error(String.format(
                    "Write to BitStream out of range! (integer: %d, bits: %d)",
                    value, count));
        }

        ensureCapacity();

        writeBits(value, count);
    }
    
    public void writePositiveIntMax1(int value) {
        writePositiveInt(value, 1);
    }
    
    public void writePositiveIntMax3(int value) {
        writePositiveInt(value, 2);
    }
    
    public void writePositiveIntMax7(int value) {
        writePositiveInt(value, 3);
    }
    
    public void writePositiveIntMax15(int value) {
        writePositiveInt(value, 4);
    }
    
    public void writePositiveIntMax31(int value) {
        writePositiveInt(value, 5);
    }
    
    public void writePositiveIntMax63(int value) {
        writePositiveInt(value, 6);
    }
    
    public void writePositiveIntMax127(int value) {
        writePositiveInt(value, 7);
    }
    
    public void writePositiveIntMax255(int value) {
        writePositiveInt(value, 8);
    }
    
    public void writePositiveIntMax511(int value) {
        writePositiveInt(value, 9);
    }
    
    public void writePositiveIntMax1023(int value) {
        writePositiveInt(value, 10);
    }
    
    public void writePositiveIntMax2047(int value) {
        writePositiveInt(value, 11);
    }
    
    public void writePositiveIntMax4095(int value) {
        writePositiveInt(value, 12);
    }
    
    public void writePositiveIntMax8191(int value) {
        writePositiveInt(value, 13);
    }
    
    public void writePositiveIntMax16383(int value) {
        writePositiveInt(value, 14);
    }
    
    public void writePositiveIntMax32767(int value) {
        writePositiveInt(value, 15);
    }
    
    public void writePositiveIntMax65535(int value) {
        writePositiveInt(value, 16);
    }
    
    public void writePositiveIntMax131071(int value) {
        writePositiveInt(value, 17);
    }
    
    public void writePositiveIntMax262143(int value) {
        writePositiveInt(value, 18);
    }
    
    public void writePositiveIntMax524287(int value) {
        writePositiveInt(value, 19);
    }
    
    public void writePositiveIntMax1048575(int value) {
        writePositiveInt(value, 20);
    }
    
    public void writePositiveIntMax2097151(int value) {
        writePositiveInt(value, 21);
    }
    
    public void writePositiveIntMax4194303(int value) {
        writePositiveInt(value, 22);
    }
    
    public void writePositiveIntMax8388607(int value) {
        writePositiveInt(value, 23);
    }
    
    public void writePositiveIntMax16777215(int value) {
        writePositiveInt(value, 24);
    }
    
    public void writePositiveIntMax33554431(int value) {
        writePositiveInt(value, 25);
    }
    
    public void writePositiveIntMax67108863(int value) {
        writePositiveInt(value, 26);
    }
    
    public void writePositiveIntMax134217727(int value) {
        writePositiveInt(value, 27);
    }

    public int readPositiveVIntMax255() {
        int count = readBits(3) + 1;
        return readBits(count);
    }

    public int readPositiveVIntMax255OftenZero() {
        if (readOneBit() == 1)
            return 0;

        int count = readBits(3) + 1;
        return readBits(count);
    }

    public int readPositiveVIntMax65535() {
        int count = readBits(4) + 1;
        return readBits(count);
    }

    public int readPositiveVIntMax65535OftenZero() {
        if (readOneBit() == 1)
            return 0;

        int count = readBits(4) + 1;
        return readBits(count);
    }

    public int readPositiveVIntMax2147483647() {
        int count = readBits(5) + 1;
        return readBits(count);
    }

    public int readPositiveVIntMax2147483647OftenZero() {
        if (readOneBit() == 1)
            return 0;

        int count = readBits(5) + 1;
        return readBits(count);
    }

    public void writePositiveVInt(int value, int count) {
        if (value != LogicMath.clamp(value, 0, ~(-1 << (1 << count)))) {
            Debugger.error(String.format(
                    "Write to BitStream out of range! (integer: %d, bits: %d)",
                    value, count));
        }

        ensureCapacity();

        int numBits;
        if (value == 0) {
            numBits = 1;
        } else {
            numBits = 32 - Integer.numberOfLeadingZeros(value);
        }

        writeBits(numBits - 1, count);
        writeBits(value, numBits);
    }
    
    public void writePositiveVIntMax255(int value) {
        writePositiveVInt(value, 3);
    }
    
    public void writePositiveVIntMax255OftenZero(int value) {
        if (value == 0) {
            writePositiveInt(1, 1);
            return;
        }

        writePositiveInt(0, 1);
        writePositiveVInt(value, 3);
    }
    
    public void writePositiveVIntMax65535(int value) {
        writePositiveVInt(value, 4);
    }
    
    public void writePositiveVIntMax65535OftenZero(int value) {
        if (value == 0) {
            writePositiveInt(1, 1);
            return;
        }

        writePositiveInt(0, 1);
        writePositiveVInt(value, 4);
    }

    public void writePositiveVIntMax2147483647(int value) {
        writePositiveVInt(value, 5);
    }

    public void writePositiveVIntMax2147483647OftenZero(int value) {
        if (value == 0) {
            writePositiveInt(1, 1);
            return;
        }

        writePositiveInt(0, 1);
        writePositiveVInt(value, 5);
    }
}
