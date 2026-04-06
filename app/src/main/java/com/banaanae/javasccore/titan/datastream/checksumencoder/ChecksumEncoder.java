package com.banaanae.javasccore.titan.datastream.checksumencoder;

import com.banaanae.javasccore.titan.LogicLong;

public class ChecksumEncoder {
    private long checksum = 0;
    private long snapshotChecksum = 0;
    private boolean enabled = false;
    
    public ChecksumEncoder() {
        this.enabled = true;
    }
    
    public void enableChecksum(boolean enable) {
        if (!this.enabled || enable) {
            if (!this.enabled && enable) {
                this.checksum = this.snapshotChecksum;
            }
            
            this.enabled = enable;
        } else {
            this.snapshotChecksum = this.checksum;
            this.enabled = false;
        }
    }
    
    public void resetChecksum() {
        this.checksum = 0;
    }
    
    public void writeBoolean(boolean booleanValue) {
        this.checksum = (booleanValue ? 13 : 7) + rotateRight(this.checksum, 31);
    }
    
    public void writeByte(byte byteValue) {
        this.checksum = byteValue + rotateRight(this.checksum, 31) + 11;
    }
    
    public void writeShort(short shortValue) {
        this.checksum = shortValue + rotateRight(this.checksum, 31) + 19;
    }
    
    public void writeInt(int intValue) {
        this.checksum = intValue + this.rotateRight(this.checksum, 31) + 9;
    }
    
    public void writeInt8(byte int8Value) {
        this.checksum = int8Value + rotateRight(this.checksum, 31) + 11;
    }
    
    public void writeInt16(short int16Value) {
        this.checksum = int16Value + rotateRight(this.checksum, 31) + 19;
    }
        
    public void writeInt24(int int24Value) {
        this.checksum = (int24Value & 0xFFFFFF) + rotateRight(this.checksum, 31) + 21;
    }
    
    public void writeVInt(int vintValue) {
        this.checksum = vintValue + rotateRight(this.checksum, 31) + 33;
    }
    
    public void writeLongLong(long longLongValue) {
        final int high = LogicLong.getHigherInt(longLongValue);
        final int low = LogicLong.getLowerInt(longLongValue);
        
        this.checksum = high + low + this.rotateRight(low + this.rotateRight(this.checksum, 31) + 67, 31) + 91;
    }
    
    public void writeVLong(long vlongValue) {
        final int high = LogicLong.getHigherInt(vlongValue);
        final int low = LogicLong.getLowerInt(vlongValue);
        
        this.checksum = low + this.rotateRight(high + this.rotateRight(this.checksum, 31) + 65, 31) + 88;
    }
    
    public void writeString(String stringValue) {
        this.checksum = (stringValue != null ? stringValue.length() + 28 : 27) + this.rotateRight(this.checksum, 31);
    }
    
    public void writeStringReference(String stringValue) {
        this.checksum = stringValue.length() + this.rotateRight(this.checksum, 31) + 38;
    };
    
    public void writeFilteredStringReference(String stringValue) {
        writeStringReference(stringValue);
    }

    public void writeBytes(byte[] buffer, int length) {
        // TODO: Buffer validation
        this.checksum = (length != 0 ? length + 38 : 37) + this.rotateRight(this.checksum, 31);
    }
    
    public boolean isChecksumEnabled() {
        return enabled;
    }
    
    public boolean isChecksumOnlyMode() {
        return true;
    }
    
    public boolean equals(ChecksumEncoder encoder) {
        long testChecksum = encoder.enabled ? encoder.checksum : encoder.snapshotChecksum;
        long currentChecksum = enabled ? checksum : snapshotChecksum;

        return testChecksum == currentChecksum;
    }
    
    private long rotateRight(long checksum, int count) {
        return (checksum >> count) | (checksum << (32 - count));
    }
}
