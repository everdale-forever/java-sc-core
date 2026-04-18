package com.banaanae.javasccore.titan.datastream.bytestream;

import com.banaanae.javasccore.titan.ArrayUtils;
import com.banaanae.javasccore.titan.Debugger;
import com.banaanae.javasccore.titan.LogicLong;
import com.banaanae.javasccore.titan.datastream.checksumencoder.ChecksumEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteStream extends ChecksumEncoder {
    public byte[] buffer;
    public int length = 0;
    int offset = 0;
    int bitOffset = 0;
    
    public ByteStream(byte[] bytes) {
        super();
        this.buffer = bytes;
        this.length = 0;
        this.offset = 0;
        this.bitOffset = 0;
    }
    
    public int readInt() {
        this.bitOffset = 0;
        return ((this.buffer[this.offset++] & 0xFF) << 24 | 
                (this.buffer[this.offset++] & 0xFF) << 16 |
                (this.buffer[this.offset++] & 0xFF) << 8 |
                (this.buffer[this.offset++] & 0xFF));
    }
    
    @Override
    public void writeInt(int intValue) {
        super.writeInt(intValue);
        
        this.bitOffset = 0;
        this.ensureCapacity(4);
        this.buffer[this.offset++] = (byte) (intValue >>> 24);
        this.buffer[this.offset++] = (byte) (intValue >>> 16);
        this.buffer[this.offset++] = (byte) (intValue >>> 8);
        this.buffer[this.offset++] = (byte) (intValue);
    }
    
    public int readIntLE() {
        this.bitOffset = 0;
        return ((this.buffer[this.offset++] & 0xFF) | 
                (this.buffer[this.offset++] & 0xFF) << 8 |
                (this.buffer[this.offset++] & 0xFF) << 16 |
                (this.buffer[this.offset++] & 0xFF) << 24);
    }
    
    public void writeIntLE(int intValue) {
        this.bitOffset = 0;
        this.ensureCapacity(4);
        this.buffer[this.offset++] = (byte) (intValue);
        this.buffer[this.offset++] = (byte) (intValue >>> 8);
        this.buffer[this.offset++] = (byte) (intValue >>> 16);
        this.buffer[this.offset++] = (byte) (intValue >>> 24);  
    }
    
    public String readString() {
        // TODO: max length
        final int stringLen = this.readInt();
        
        if (stringLen < 0) {
            System.out.println(String.format("Negative String length encountered. (%d)", length));
            return "";
        } else if (stringLen > 900000) {
            System.out.println(String.format("Too long String encountered, length %d, max %d.", length, 900000));
            return "";
        } else {
            String s = new String(this.buffer, offset, stringLen, StandardCharsets.UTF_8);
            return s;
        }
    }
    
    @Override
    public void writeString(String stringValue) {
        super.writeString(stringValue);

        if (stringValue == null) {
            this.writeInt(-1);
            return;
        }

        byte[] bytes = stringValue.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > 900_000) {
            Debugger.warning(String.format("ByteStream::writeString invalid string byte length %d", bytes.length));
            writeInt(-1);
            return;
        } else if (bytes.length == 0) {
            writeInt(-1);
            return;
        }

        writeInt(bytes.length);
        ensureCapacity(bytes.length);
        System.arraycopy(bytes, 0, buffer, offset, bytes.length);
        
        this.offset += bytes.length;
    }
    
    public int readVInt() {
        int result = 0;
        int shift = 0;
        int reconstructed = 0;
        int signBit = 0;
        int continuationFlag = 0;

        while (true) {
            byte currByte = this.buffer[this.offset++];
            if (shift == 0) {
                signBit = (currByte & 0x40) >> 6;
                continuationFlag = (currByte & 0x80) >> 7;
                reconstructed = (currByte << 1) & ~0x181;
                currByte = (byte) (reconstructed | (continuationFlag << 7) | signBit);
            }

            result |= (currByte & 0x7F) << shift;
            shift += 7;
            if ((currByte & 0x80) == 0) {
                break;
            }
        }

        return (result >> 1) ^ (-(result & 1));
    }
    
    @Override
    public void writeVInt(int vintValue) {
        super.writeVInt(vintValue);

        this.bitOffset = 0;
        int temp = (vintValue >> 25) & 0x40;
        int flipped = vintValue ^ (vintValue >> 31);

        temp |= vintValue & 0x3F;

        vintValue >>= 6;
        flipped >>= 6;

        if (flipped == 0) {
            this.writeByte((byte) temp);
            return;
        }

        this.writeByte((byte) (temp | 0x80));

        vintValue >>= 0;
        while (true) {
            int continuationBit = (flipped >> 7) != 0 ? 0x80 : 0;
            this.writeByte((byte) ((vintValue & 0x7F) | continuationBit));

            vintValue >>>= 7;
            flipped >>>= 7;

            if (flipped == 0) {
                break;
            }
        }
    }
    
    public boolean readBoolean() {
        final int newBitOffset = bitOffset;
        final int newOffset = offset + (8 - newBitOffset >> 3);
        
        this.offset = newOffset;
        this.bitOffset = newBitOffset + 1 & 7;
        
        return (1 << (newBitOffset & 7) & this.buffer[newOffset - 1]) != 0;
    }
    
    @Override
    public void writeBoolean(boolean booleanValue) {
        super.writeBoolean(booleanValue);
        
        if (bitOffset == 0) {
            this.ensureCapacity(1);
            this.buffer[this.offset++] = 0;
        }
        
        if (booleanValue) {
            this.buffer[offset - 1] |= (1 << bitOffset);
        }

        this.bitOffset = (bitOffset + 1) & 7;
    }
    
    public String readStringReference() {
        final int strLength = readInt();
        int maxCapacity = 900000;

        if (maxCapacity < 0)
            Debugger.warning("Negative String reference length encountered.");

        if (strLength > maxCapacity)
            Debugger.warning("Too long String reference encountered, max", String.valueOf(maxCapacity));

        if (length > 0 && strLength <= maxCapacity) {
            final String string = new String(this.buffer, offset, strLength, StandardCharsets.UTF_8);
            this.offset += strLength;
            return string;
        }

        return "";
    }
    
    @Override
    public void writeStringReference(String stringReferenceValue) {
        super.writeStringReference(stringReferenceValue);
        
        if (stringReferenceValue == null) {
            writeInt(0);
            return;
        }
        
        byte[] bytes = stringReferenceValue.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > 900000) {
            Debugger.warning(String.format("Too long String reference encountered, max %d ", 900000));
            writeInt(0);
            return;
        }
        
        writeInt(bytes.length);
        ensureCapacity(bytes.length);
        System.arraycopy(bytes, 0, buffer, offset, bytes.length);
    }
    
    public long readLongLong() {
        int high = this.readInt();
        int low = this.readInt();
        
        return LogicLong.toLong(high, low);
    }
    
    @Override
    public void writeLongLong(long longLongValue) {
        super.writeLongLong(longLongValue);
        
        this.writeInt(LogicLong.getHigherInt(longLongValue));
        this.writeInt(LogicLong.getLowerInt(longLongValue));
    }
    
    public LogicLong readLong() {
        return new LogicLong(this.readInt(), this.readInt());
    }
    
    public void writeLong(LogicLong logicLongValue) {
        logicLongValue.encode(this);
    }
    
    public byte readByte() {
        return this.buffer[this.offset++];
    }
    
    @Override
    public void writeByte(byte byteValue) {
        super.writeByte(byteValue);
        this.bitOffset = 0;
        this.ensureCapacity(1);
        this.buffer[this.offset++] = byteValue;
    }
    
    public byte[] readBytes() {
        final int bytesLen = this.readBytesLength();
        final byte[] bytes = Arrays.copyOfRange(buffer, offset, offset + bytesLen);
        this.offset += bytesLen;
        return bytes;
    }
    
    public int readBytesLength() {
        return this.readInt();
    }
    
    @Override
    public void writeBytes(byte[] bytesValue, int length) {
        super.writeBytes(bytesValue, length);
        
        this.writeInt(length);
        this.writeBytesWithoutLength(bytesValue);
        this.offset += length;
        // TODO: null value -1
    }
    
    public void writeBytesWithoutLength(byte[] bytesValue) {
        buffer = ArrayUtils.concat(buffer, bytesValue);
        this.offset += bytesValue.length;
    }
    
    public int readInt8() {
        this.bitOffset = 0;
        return this.buffer[this.offset++];
    }
    
    @Override
    public void writeInt8(byte int8Value) {
        this.bitOffset = 0;
        this.ensureCapacity(1);
        this.buffer[this.offset++] = (byte) (int8Value);
    }
    
    public int readInt16() {
        this.bitOffset = 0;
        return (this.buffer[this.offset++] << 8 |
                this.buffer[this.offset++]);
    }
    
    @Override
    public void writeInt16(short int16Value) {
        this.bitOffset = 0;
        this.ensureCapacity(2);
        this.buffer[this.offset++] = (byte) (int16Value >>> 8);
        this.buffer[this.offset++] = (byte) (int16Value);
    }
    
    public int readInt24() {
        this.bitOffset = 0;
        return (this.buffer[this.offset++] << 16 |
                this.buffer[this.offset++] << 8 |
                this.buffer[this.offset++]);
    }
    
    @Override
    public void writeInt24(int int24Value) {
        this.bitOffset = 0;
        this.ensureCapacity(3);
        this.buffer[this.offset++] = (byte) (int24Value >>> 16);
        this.buffer[this.offset++] = (byte) (int24Value >>> 8);
        this.buffer[this.offset++] = (byte) (int24Value);
    }
    
    public long readVLong() {
        this.bitOffset = 0;

        byte b = buffer[offset++];
        boolean negative = (b & 0x40) != 0;
        long result = b & 0x3F;

        if ((b & 0x80) == 0) {
            return negative ? result | 0xFFFFFFC0L : result;
        }

        result |= (buffer[offset++] & 0x7FL) << 6;
        if ((buffer[offset - 1] & 0x80) == 0) {
            return negative ? result | 0xFFFFE000L : result;
        }

        result |= (buffer[offset++] & 0x7FL) << 13;
        if ((buffer[offset - 1] & 0x80) == 0) {
            return negative ? result | 0xFFF00000L : result;
        }

        result |= (buffer[offset++] & 0x7FL) << 20;
        if ((buffer[offset - 1] & 0x80) == 0) {
            return negative ? result | 0xF8000000L : result;
        }

        result |= (buffer[offset++] & 0x7FL) << 27;
        if ((buffer[offset - 1] & 0x80) == 0) {
            return result;
        }

        // skip remaining bytes idk why
        for (int i = 0; i < 4 && (buffer[offset] & 0x80) != 0; i++) {
            offset++;
        }
        offset++; // termination byte

        return result;
    }
    
    @Override
    public void writeVLong(long value) {
        this.bitOffset = 0;
        ensureCapacity(10);

        if (value >= 0) {
            if (value <= 0x3F) {
                buffer[offset++] = (byte) value;
                return;
            }
            if (value <= 0x1FFF) {
                buffer[offset++] = (byte) ((value & 0x3F) | 0x80);
                buffer[offset++] = (byte)  (value >>> 6);
                return;
            }
            if (value <= 0xFFFFF) {
                buffer[offset++] = (byte) ((value & 0x3F) | 0x80);
                buffer[offset++] = (byte) ((value >>> 6)  | 0x80);
                buffer[offset++] = (byte)  (value >>> 13);
                return;
            }
            if (value <= 0x7FFFFFFL) {
                buffer[offset++] = (byte) ((value & 0x3F) | 0x80);
                buffer[offset++] = (byte) ((value >>> 6)  | 0x80);
                buffer[offset++] = (byte) ((value >>> 13) | 0x80);
                buffer[offset++] = (byte)  (value >>> 20);
                return;
            }

            int high32 = (int) (value >>> 32);
            int numCont = 3;
            if (high32 >= 0x40000000) numCont = 7;
            else if (high32 >= 0x800000) numCont = 6;
            else if (high32 >= 0x10000) numCont = 5;
            else if (high32 >= 512) numCont = 4;

            buffer[offset++] = (byte) ((value & 0x3F) | 0x80);
            int shift = 6;
            for (int i = 0; i < numCont; i++) {
                buffer[offset++] = (byte) ((value >>> shift) | 0x80);
                shift += 7;
            }
            buffer[offset++] = (byte) (value >>> shift);

        } else {
            if (value >= -63) {
                buffer[offset++] = (byte) ((value & 0x3F) | 0x40);
                return;
            }
            if (value > -8192) {
                buffer[offset++] = (byte) (value | 0xC0);
                buffer[offset++] = (byte) (value >>> 6);
                return;
            }
            if (value > -1048576) {
                buffer[offset++] = (byte)  (value | 0xC0);
                buffer[offset++] = (byte) ((value >>> 6) | 0x80);
                buffer[offset++] = (byte)  (value >>> 13);
                return;
            }
            if (value > -134217728) {
                buffer[offset++] = (byte) (value | 0xC0);
                buffer[offset++] = (byte) ((value >>> 6) | 0x80);
                buffer[offset++] = (byte) ((value >>> 13) | 0x80);
                buffer[offset++] = (byte) (value >>> 20);
                return;
            }

            int high32 = (int) (value >>> 32);
            int adjusted = high32 + (value != 0 ? 1 : 0);
            int numCont = 3;
            if (adjusted < -1073741824) numCont = 7;
            else if (adjusted < -8388608) numCont = 6;
            else if (adjusted < -65536) numCont = 5;
            else if (adjusted < -512) numCont = 4;

            buffer[offset++] = (byte) (value | 0xC0);
            int shift = 6;
            for (int i = 0; i < numCont; i++) {
                buffer[offset++] = (byte) ((value >>> shift) | 0x80);
                shift += 7;
            }
            buffer[offset++] = (byte) (value >>> shift);
        }
    }
    
    public String readFilteredString() {
        System.out.println("TODO: readFilteredString");
        return "";
    }
    
    public void writeFilteredString(String filteredStringValue) {
        System.out.println("TODO: writeFilteredString");
    }
    
    public String readFilteredStringReference() {
        System.out.println("TODO: readFilteredStringReference");
        return "";
    }
    
    @Override
    public void writeFilteredStringReference(String filteredStringReferenceValue) {
        System.out.println("TODO: writeFilteredStringReference");
    }
    
    private void ensureCapacity(int capacity) {
        final int len = this.buffer.length;
        
        if (offset + capacity > len) {
            this.buffer = ArrayUtils.concat(buffer, new byte[capacity]);
        }
    }
}
