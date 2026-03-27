package com.banaanae.javasccore.titan.datastream.bitstream;

public class BitStream {
    byte[] stream;
    int offset = 0;
    int bitOffset = 0;
    
    public BitStream(byte[] bytes) {
        this.stream = bytes;
        this.offset = 0;
        this.bitOffset = 0;
    }
    
    // Later
}
