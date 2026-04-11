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
    
    public int readBits(int count) {
        if (count < 1)
            return 0;
        
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != count; ++i) {
            byte currByte = stream[offset];
            this.bitOffset = oldBitOffset + 1;
            
            result |= ((currByte & (1 << oldBitOffset)) >> oldBitOffset) << i;
            
            if (bitOffset == 7) {
                this.offset++;
                this.bitOffset = 0;
            } else {
                oldBitOffset++;
            }
        }
        
        return result;
    }
    
    public boolean readBoolean() {
        byte currByte = stream[offset];
        int bit = (currByte & (1 << bitOffset)) >> bitOffset;
        
        if (bitOffset == 7) {
            this.offset++;
            this.bitOffset = 0;
        }
        
        return bit == 1;
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
    
    public int readIntMax1() {
        int sign = readOneBit();

        int magnitude = readOneBit();

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax3() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 2; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax7() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 3; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax15() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 4; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax31() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 5; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax63() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 6; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax127() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 7; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax255() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 8; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax511() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 9; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax1023() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 10; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax2047() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 11; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax4095() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 12; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax8191() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 13; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax16383() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 14; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax32767() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 15; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    public int readIntMax65535() {
        int sign = readOneBit();

        int magnitude = 0;
        for (int i = 0; i < 16; i++)
            magnitude |= (readOneBit() << i);

        return magnitude * (2 * sign - 1);
    }
    
    // Doesn't seem to resize, nor does it have any xrefs...
    public void writeBits(int value, int bitCount) {
        if (offset == 0 && bitOffset == 0)
            stream[0] = 0;

        for (int i = 0; i < bitCount; i++) {
            int bit = (value >> i) & 1;
            stream[offset] |= (bit << bitOffset);

            bitOffset++;

            if (bitOffset == 8) {
                bitOffset = 0;
                offset++;

                if (offset < stream.length) {
                    stream[offset] = 0;
                }
            }
        }
    }
    
    public void writeBoolean(boolean value) {
        writePositiveInt(value ? 1 : 0, 1);
    }
    
    public void writeOneBit(int value) {
        stream[offset] |= value << bitOffset;
        
        this.bitOffset++;
        if (bitOffset == 8) {
            this.offset++;
            this.bitOffset = 0;
        }
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
        if (value != LogicMath.clamp(value, (-1 << bitsCount) + 1, ~(-1 << bitsCount)))
            Debugger.error(String.format(
                    "Write to BitStream out of range! (integer: %d, bits: %d)"
                    , value, bitsCount));
        
        if (offset + 6 > capacity) {
            int newCap = capacity + 105;
            byte[] newStream = new byte[newCap];

            System.arraycopy(this.stream, 0, newStream, 0, offset + 1);

            this.stream = newStream;
            this.capacity = newCap;
        }
        
        writeOneBit(value >= 0 ? 1 : 0);
        this.bitOffset++;
        if (bitOffset == 8) {
            this.offset++;
            this.bitOffset = 0;
            stream[offset] = 0;
        }

        if (bitsCount >= 1) {
            int bitIdx = 0;

            do {
                stream[offset] |= (LogicMath.abs(value) & (1 << bitIdx)) >> bitIdx << bitOffset;
                bitOffset++;
                if (bitOffset == 8) {
                    this.offset++;
                    this.bitOffset = 0;
                    stream[offset] = 0;
                }
                bitIdx++;
            } while (bitIdx != bitsCount);
        }
    }
    
    public int readPositiveIntMax1() {
        int currByte = stream[offset];
        bitOffset++;
        if (bitOffset == 8) {
            bitOffset = 0;
            offset++;
        }
        return (currByte & (1 << bitOffset)) >> bitOffset;
    }
    
    public int readPositiveIntMax3() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 2; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax7() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 3; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax15() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 4; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax31() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 5; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax63() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 6; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax127() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 7; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax255() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 8; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax511() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 9; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax1023() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 10; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax2047() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 11; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax4095() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 12; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax8191() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 13; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax16383() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 14; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax32767() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 15; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax65535() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 16; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax131071() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 17; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax262143() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 18; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax524287() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 19; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax1048575() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 20; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax2097151() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 21; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax4194303() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 22; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    // no 8388608? TODO: Check later than 36
    
    public int readPositiveIntMax16777215() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 2; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax33554431() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 25; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax67108863() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 26; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public int readPositiveIntMax134217727() {
        int result = 0;
        int oldBitOffset = bitOffset;
        
        for (int i = 0; i != 27; ++i) {
            int currByte = stream[offset];
            bitOffset = oldBitOffset + 1;
            int v7 = (currByte & (1 << oldBitOffset)) >> oldBitOffset;
            if (oldBitOffset == 7) {
                offset++;
                bitOffset = 0;
                oldBitOffset = 0;
            } else {
                ++oldBitOffset;
            }
            result |= v7 << i;
        }
        return result;
    }
    
    public void writePositiveInt(int value, int bitsCount) {
        if (value != LogicMath.clamp(value, 0, ~(-1 << bitsCount)))
            Debugger.error(String.format(
                    "Write to BitStream out of range! (integer: %d, bits: %d)"
                    , value, bitsCount));
        
        if (offset + 6 > capacity) {
            int newCap = capacity + 105;
            byte[] newStream = new byte[newCap];

            System.arraycopy(this.stream, 0, newStream, 0, offset + 1);

            this.stream = newStream;
            this.capacity = newCap;
        }
        
        if (bitsCount >= 1) {
            int bitsIdx = 0;
            
            do {
                stream[offset] |= (value & (1 << bitsIdx)) >> bitsIdx << bitOffset;
                this.bitOffset++;
                if (bitOffset == 8) {
                    this.offset++;
                    this.bitOffset = 0;
                }
                bitsIdx++;
            } while (bitsIdx != bitsCount);
        }
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
    
    /* TODO
    public int readPositiveVIntMax255() {}
    public int readPositiveVIntMax255OftenZero() {}
    public int readPositiveVIntMax65535() {}
    public int readPositiveVIntMax65535OftenZero() {}
    public long readPositiveVIntMax4294967295() {return 0;}
    public long readPositiveVIntMax4294967295OftenZero() {return 0;}*/
    
    public void writePositiveVInt(int value, int bitsCount) {
        if (value != LogicMath.clamp(value, 0, ~(-1 << (1 << bitsCount))))
            Debugger.error(String.format(
                    "Write to BitStream out of range! (integer: %d, bits: %d)"
                    , value, bitsCount));
        
        if (offset + 6 > capacity) {
            int newCap = capacity + 105;
            byte[] newStream = new byte[newCap];

            System.arraycopy(this.stream, 0, newStream, 0, offset + 1);

            this.stream = newStream;
            this.capacity = newCap;
        }
        
        int v13;
        int v14;
        if (value != 0) {
            if (value < 1) {
                v14 = 0;
            } else {
                v13 = value;
                v14 = 0;
                do {
                    v14++;
                    v13 >>= 1;
                } while (v13 != 0);
            }
        } else {
            v14 = 1;
        }
        
        if (bitsCount >= 1) {
            int bitIdx = 0;
            do {
                stream[offset] |= ((v14 - 1) & (1 << bitIdx)) >> bitIdx << bitOffset;
                this.bitOffset++;
                if (bitOffset == 8) {
                    this.offset++;
                    this.bitOffset = 0;
                }
                bitIdx++;
            } while (bitIdx != bitOffset);
        }
        
        if (v14 != 0) {
            for (int i = 0; i != v14; ++i) {
                stream[offset] |= ((value & (1 << i)) >> i) << bitOffset;
                this.bitOffset++;
                if (bitOffset == 8) {
                    this.offset++;
                    this.bitOffset = 0;
                }
            }
        }
    }
    
    public void writePositiveVIntMax255(int value) {
        writePositiveInt(value, 3);
    }
    
    public void writePositiveVIntMax255OftenZero(int value) {
        writePositiveInt(0, 1);
        
        if (value == 0)
            return;
        
        writePositiveVInt(value, 3);
    }
    
    public void writePositiveVIntMax65535(int value) {
        writePositiveInt(value, 4);
    }
    
    public void writePositiveVIntMax65535OftenZero(int value) {
        writePositiveInt(0, 1);
        
        if (value == 0)
            return;
        
        writePositiveVInt(value, 4);
    }
    
    // TODO
    public void writePositiveVIntMax4294967295(long value) {} // 5
    public void writePositiveVIntMax4294967295OftenZero(long value) {}
}
