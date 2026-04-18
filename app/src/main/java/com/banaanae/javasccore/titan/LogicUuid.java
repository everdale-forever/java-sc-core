package com.banaanae.javasccore.titan;

import com.banaanae.javasccore.titan.datastream.bytestream.ByteStream;
import com.banaanae.javasccore.titan.datastream.checksumencoder.ChecksumEncoder;

public class LogicUuid {
    long high;
    long low;
    
    public LogicUuid(long high, long low) {
        this.high = high;
        this.low = low;
    }
    
    public LogicUuid() {
        this.high = 0;
        this.low = 0;
    }
    
    public void copyFrom(LogicUuid uuid) {
        this.high = uuid.high;
        this.low = uuid.low;
    }
    
    public void encode(ChecksumEncoder stream) {
        stream.writeVLong(high);
        stream.writeVLong(low);
    }
    
    public void decode(ByteStream stream) {
        this.high = stream.readVLong();
        this.low = stream.readVLong();
    }
}
