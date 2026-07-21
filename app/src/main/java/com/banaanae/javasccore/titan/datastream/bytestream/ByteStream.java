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
    public void writeInt(int value) {
        super.writeInt(value);

        this.bitOffset = 0;
        this.ensureCapacity(4);
        this.buffer[this.offset++] = (byte) (value >>> 24);
        this.buffer[this.offset++] = (byte) (value >>> 16);
        this.buffer[this.offset++] = (byte) (value >>> 8);
        this.buffer[this.offset++] = (byte) (value);
    }

    public int readIntLE() {
        this.bitOffset = 0;
        return (this.buffer[this.offset++] |
                this.buffer[this.offset++] << 8 |
                this.buffer[this.offset++] << 16 |
                this.buffer[this.offset++] << 24);
    }

    public void writeIntLE(int value) {
        this.bitOffset = 0;
        this.ensureCapacity(4);
        this.buffer[this.offset++] = (byte) (value);
        this.buffer[this.offset++] = (byte) (value >>> 8);
        this.buffer[this.offset++] = (byte) (value >>> 16);
        this.buffer[this.offset++] = (byte) (value >>> 24);
    }

    public String readString() {
        // TODO: max length
        final int stringLen = this.readInt();

        if (stringLen < 0) {
            System.out.printf("Negative String length encountered. (%d)%n", length);
            return "";
        } else if (stringLen > 900000) {
            System.out.printf("Too long String encountered, length %d, max %d.%n", length, 900000);
            return "";
        } else {
            return new String(this.buffer, offset, stringLen, StandardCharsets.UTF_8);
        }
    }

    @Override
    public void writeString(String value) {
        super.writeString(value);

        if (value == null) {
            this.writeInt(-1);
            return;
        }

        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
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
    public void writeVInt(int value) {
        super.writeVInt(value);

        ensureCapacity(4);
        this.bitOffset = 0;
        int temp = (value >> 25) & 0x40;
        int flipped = value ^ (value >> 31);

        temp |= value & 0x3F;

        value >>= 6;
        flipped >>= 6;

        if (flipped == 0) {
            this.writeByte((byte) temp);
            return;
        }

        this.writeByte((byte) (temp | 0x80));

        value >>= 0;
        do {
            int continuationBit = (flipped >> 7) != 0 ? 0x80 : 0;
            this.writeByte((byte) ((value & 0x7F) | continuationBit));

            value >>>= 7;
            flipped >>>= 7;
        } while (flipped != 0);
    }

    public boolean readBoolean() {
        final int newBitOffset = bitOffset;
        final int newOffset = offset + (8 - newBitOffset >> 3);

        this.offset = newOffset;
        this.bitOffset = newBitOffset + 1 & 7;

        return (1 << (newBitOffset & 7) & this.buffer[newOffset - 1]) != 0;
    }

    @Override
    public void writeBoolean(boolean value) {
        super.writeBoolean(value);

        if (bitOffset == 0) {
            this.ensureCapacity(1);
            this.buffer[this.offset++] = 0;
        }

        if (value) {
            this.buffer[offset - 1] |= (byte) (1 << bitOffset);
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

        if (stringReferenceValue.isEmpty()) {
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

        return ((long) high << 32) | (low & 0xFFFFFFFFL);
    }

    @Override
    public void writeLongLong(long value) {
        super.writeLongLong(value);

        ensureCapacity(8);
        this.writeInt((int) (value >>> 32));
        this.writeInt((int) value);
    }

    public LogicLong readLong() {
        return new LogicLong(this.readInt(), this.readInt());
    }

    public void writeLong(LogicLong value) {
        ensureCapacity(8);
        value.encode(this);
    }

    public byte readByte() {
        return this.buffer[this.offset++];
    }

    @Override
    public void writeByte(byte value) {
        super.writeByte(value);
        this.bitOffset = 0;
        this.ensureCapacity(1);
        this.buffer[this.offset++] = value;
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
    public void writeBytes(byte[] bytes, int length) {
        super.writeBytes(bytes, length);

        ensureCapacity(length);
        this.writeInt(length);
        this.writeBytesWithoutLength(bytes);
        this.offset += length;
        // TODO: null value -1
    }

    public void writeBytesWithoutLength(byte[] bytes) {
        if (bytes == null) return;
        ensureCapacity(bytes.length);
        System.arraycopy(bytes, 0, this.buffer, this.offset, bytes.length);
        this.offset += bytes.length;
    }

    public int readInt8() {
        this.bitOffset = 0;
        return this.buffer[this.offset++];
    }

    @Override
    public void writeInt8(byte value) {
        this.bitOffset = 0;
        this.ensureCapacity(1);
        this.buffer[this.offset++] = value;
    }

    public int readInt16() {
        this.bitOffset = 0;
        return (this.buffer[this.offset++] << 8 |
                this.buffer[this.offset++]);
    }

    @Override
    public void writeInt16(short value) {
        this.bitOffset = 0;
        this.ensureCapacity(2);
        this.buffer[this.offset++] = (byte) (value >>> 8);
        this.buffer[this.offset++] = (byte) (value);
    }

    public int readInt24() {
        this.bitOffset = 0;
        return (this.buffer[this.offset++] << 16 |
                this.buffer[this.offset++] << 8 |
                this.buffer[this.offset++]);
    }

    @Override
    public void writeInt24(int value) {
        this.bitOffset = 0;
        this.ensureCapacity(3);
        this.buffer[this.offset++] = (byte) (value >>> 16);
        this.buffer[this.offset++] = (byte) (value >>> 8);
        this.buffer[this.offset++] = (byte) (value);
    }

    public long readVLong() {
        int b = this.buffer[this.offset++] & 0xFF;

        boolean negative = (b & 0x40) != 0;
        long value = b & 0x3f;
        int shift = 6;

        while ((b & 0x80) != 0) {
            b = this.readByte();

            value |= ((long) (b & 0x7F)) << shift;
            shift += 7;
        }

        if (negative && shift < 64) {
            value |= (long) (~0) << shift;
        }

        return value;
    }

    @Override
    public void writeVLong(long value) {
        super.writeVLong(value);

        ensureCapacity(8);
        final boolean negative = value < 0;
        long first = value & 0x3f | (negative ? 0x40 : 0);
        value >>= 6;
        final boolean done = (negative && value == -1) || (!negative && value == 0);
        if (done) {
            this.buffer[this.offset++] = (byte) first;
            return;
        }

        this.buffer[this.offset++] = (byte) (first | 0x80);
        while (true) {
            byte b = (byte) (value & 0x7f);
            value >>= 7;

            final boolean finished = (value == 0 && (b & 0x40) == 0) || (value == -1 && (b & 0x40) != 0);
            if (!finished)
                b |= (byte) 0x80;
            this.buffer[this.offset++] = b;

            if (finished)
                break;
        }
    }

    public String readFilteredString() {
        System.out.println("TODO: readFilteredString");
        return "";
    }

    public void writeFilteredString(String value) {
        System.out.println("TODO: writeFilteredString");
    }

    public String readFilteredStringReference() {
        System.out.println("TODO: readFilteredStringReference");
        return "";
    }

    @Override
    public void writeFilteredStringReference(String value) {
        System.out.println("TODO: writeFilteredStringReference");
    }

    private void ensureCapacity(int capacity) {
        final int len = this.buffer.length;

        if (offset + capacity > len) {
            this.buffer = ArrayUtils.concat(buffer, new byte[capacity]);
        }
    }
}
