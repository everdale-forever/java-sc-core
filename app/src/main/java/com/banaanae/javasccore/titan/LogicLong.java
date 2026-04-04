package com.banaanae.javasccore.titan;

import com.banaanae.javasccore.titan.datastream.bytestream.ByteStream;

public class LogicLong {
    int high = 0;
    int low = 0;
    
    public LogicLong(int high, int low) {
        this.high = high;
        this.low = low;
    }
    
    public LogicLong(int high) {
        this.high = high;
    }
        
    public LogicLong() {}
    
    public void decode(ByteStream stream) {
        this.high = stream.readInt();
        this.low = stream.readInt();
    }
    
    public void encode(ByteStream stream) {
        stream.writeInt(this.high);
        stream.writeInt(this.low);
    }
    
    public static LogicLong clone(LogicLong logicLongValue) {
        final LogicLong clonedLogicLong = new LogicLong(
                logicLongValue.high,
                logicLongValue.low
        );
        
        return clonedLogicLong;
    }
    
    @Override
    public boolean equals(Object logicLongValue) {
        if (this == logicLongValue) return true;
        if (!(logicLongValue instanceof LogicLong)) return false;
        LogicLong other = (LogicLong) logicLongValue;
        return this.high == other.high && this.low == other.low;
    }
    
    public static int getHigherInt(long longValue) {
        return (int) (longValue >>> 32);
    }
    
    public int getHigherInt(LogicLong logicLongValue) {
        return logicLongValue.high;
    }
    
    public int getHigherInt() {
        return high;
    }
    
    public static int getLowerInt(long longValue) {
        return (int) (longValue < 0 ? longValue | 0x80000000 : longValue & 0x7FFFFFFF);
    }
    
    public int getLowerInt(LogicLong logicLongValue) {
        return logicLongValue.low;
    }
    
    public int getLowerInt() {
        return low;
    }
    
    public boolean greaterThan(LogicLong logicLongValue) {
        // TODO: Object support
        return (high > logicLongValue.high ||
               (high == logicLongValue.high &&
               low > logicLongValue.low));
    }
    
    public boolean isZero() {
        return high == 0 && low == 0;
    }
    
    public void set(int high, int low) {
        // why supercell
        this.high = high >> 32;
        this.low = low < 0 ? low | 0x80000000 : low & 0x7FFFFFFF;
    }
    
    @Override
    public int hashCode() {
        return 31 * Integer.hashCode(high) + Integer.hashCode(low);
    }
    
    public static long toLong(int high, int low) {
        return (high << 32) | (low < 0 ? low | 0x80000000 : low & 0x7FFFFFFF);
    }
    
    public long toLong() {
        return ((long) high << 32) | ((long) low & 0xFFFFFFFFL);
    }
    
    public String toString(int high, int low) {
        return String.format("LogicLong(%d, %d)", high, low);
    }
}
