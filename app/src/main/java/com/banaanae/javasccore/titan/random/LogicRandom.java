package com.banaanae.javasccore.titan.random;

import com.banaanae.javasccore.titan.datastream.bytestream.ByteStream;

public class LogicRandom {
    int seed = 0;
    
    public LogicRandom(int seed) {
        this.seed = seed;
    }
    
    public int getIteratedSeed() {
        return seed;
    }
    
    public void setIteratedSeed(int seed) {
        this.seed = seed;
    }
    
    public int rand(int max) {
        if (max > 0) {
            final int seed = this.seed == 0 ? -1 : this.seed;
            
            final int first = seed ^ (seed << 13) ^ ((seed ^ (seed << 13)) >> 17);
            int second = first ^ (32 * first);
            this.setIteratedSeed(second);
            second = Math.abs(second);
            
            return second % max;
        }
        
        return 0;
    }
    
    public int iterateRandomSeed() {
        final int seed = this.seed == 0 ? -1 : this.seed;
            
        final int first = seed ^ (seed << 13) ^ ((seed ^ (seed << 13)) >> 17);
        return first ^ (32 * first);
    }
    
    public void decode(ByteStream stream) {
        this.seed = stream.readInt();
    }
    
    public void encode(ByteStream stream) {
        stream.writeInt(seed);
    }
}
