/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banaanae.javasccore.titan.datastream.bytestream;

import com.banaanae.javasccore.titan.datastream.checksumencoder.ChecksumEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Banaanae
 */
public class ByteStream extends ChecksumEncoder {
    ByteBuffer buffer;
    int length = 0;
    int offset = 0;
    int bitOffset = 0;
    
    public ByteStream(ByteBuffer bytes) {
        super();
        this.buffer = bytes;
        this.length = 0;
        this.offset = 0;
        this.bitOffset = 0;
    }
    
    public int readInt() {
        this.bitOffset = 0;
        return (this.buffer.get(this.offset++) << 24 | 
                this.buffer.get(this.offset++) << 16 |
                this.buffer.get(this.offset++) << 8 |
                this.buffer.get(this.offset++));
    }
    
    @Override
    public void writeInt(int intValue) {
        super.writeInt(intValue);
        
        this.bitOffset = 0;
        this.ensureCapacity(4);
        this.buffer.put(this.offset++, (byte) (intValue >>> 24));
        this.buffer.put(this.offset++, (byte) (intValue >>> 16));
        this.buffer.put(this.offset++, (byte) (intValue >>> 8));
        this.buffer.put(this.offset++, (byte) (intValue));  
    }
    
    public int readIntLE() {
        this.bitOffset = 0;
        return (this.buffer.get(this.offset++) | 
                this.buffer.get(this.offset++) << 8 |
                this.buffer.get(this.offset++) << 16 |
                this.buffer.get(this.offset++) << 24);
    }
    
    public void writeIntLE(int intValue) {
        this.bitOffset = 0;
        this.ensureCapacity(4);
        this.buffer.put(this.offset++, (byte) (intValue));
        this.buffer.put(this.offset++, (byte) (intValue >>> 8));
        this.buffer.put(this.offset++, (byte) (intValue >>> 16));
        this.buffer.put(this.offset++, (byte) (intValue >>> 24));  
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
            ByteBuffer src = buffer.duplicate();
            src.position(offset).limit(offset + stringLen);
            ByteBuffer slice = src.slice();
            String s = StandardCharsets.UTF_8.decode(slice).toString();
            offset += length;
            return s;
        }
    }
    
    @Override
    public void writeString(String stringValue) {
        super.writeString(stringValue);
        
        final int stringLen = stringValue.length();
        if (stringLen > 900000) {
            System.out.println(String.format("ByteStream::writeString invalid string length %d", stringLen));
        } else if (stringLen == 0) {
            this.writeInt(-1);
            return;
        }
        
        byte[] bytes = stringValue.getBytes(StandardCharsets.UTF_8);
        this.writeInt(bytes.length);
        this.ensureCapacity(bytes.length);
        this.buffer.position(offset);
        this.buffer.put(bytes);
        this.offset += bytes.length;
    }
    
    public int readVInt() {
        
    }
    
    public void writeVInt(int vintValue) {
        
    }
    
    public boolean readBoolean() {
        
    }
    
    public void writeBoolean() {
        
    }
    
    public String readStringReference() {
        
    }
    
    public void writeStringReference() {
        
    }
    
    public long readLongLong() {
        
    }
    
    public void writeLongLong() {
        
    }
    
    public long readLong() {
        
    }
    
    public void writeLong() {
        
    }
    
    public byte readByte() {
        
    }
    
    public void writeByte() {
        
    }
    
    public ByteBuffer readBytes() {
        
    }
    
    public int readBytesLength() {
        return this.readInt();
    }
    
    public void writeBytes() {
        
    }
    
    public void writeBytesWithoutLength() {
        
    }
    
    public byte readInt8() {
        
    }
    
    public void writeInt8() {
        
    }
    
    public short readInt16() {
        
    }
    
    public void writeInt16() {
        
    }
    
    public int readInt24() {
        
    }
    
    public void writeInt24() {
        
    }
    
    public long readVLong() {
        
    }
    
    public void writeVLong() {
        
    }
    
    public String readFilteredString() {
        
    }
    
    public void writeFilteredString() {
        
    }
    
    public String readFilteredStringReference() {
        
    }
    
    public void writeFilteredStringReference() {
        
    }
    
    private void ensureCapacity(int capacity) {
        final int bufferLength = buffer.capacity();
        
        if (offset + capacity > bufferLength) {
            int newCapacity = (offset + capacity);
            ByteBuffer tmp = ByteBuffer.allocate(newCapacity - bufferLength);
            ByteBuffer newBuf = ByteBuffer.allocate(newCapacity);
            buffer.position(0);
            newBuf.put(buffer);
            newBuf.put(tmp);
            newBuf.flip();
            this.buffer = newBuf;
        }
    }
}
