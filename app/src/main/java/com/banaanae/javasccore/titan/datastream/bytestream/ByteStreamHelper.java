package com.banaanae.javasccore.titan.datastream.bytestream;

public class ByteStreamHelper {
    private final ByteStream stream;
    
    public ByteStreamHelper(ByteStream stream) {
        this.stream = stream;
    }
    
    /*public ByteStreamHelper(BitStream stream) {
        System.out.println("TODO: ByteStreamHelper(BitStream)");
    }*/
    
    public void encodeIntList(int[] intArray) {
        this.stream.writeVInt(intArray.length);
        
        for (int val : intArray) {
            this.stream.writeVInt(val);
        }
    }
}
