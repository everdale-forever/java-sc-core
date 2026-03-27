package com.banaanae.javasccore.titan;

import com.banaanae.javasccore.titan.datastream.bytestream.ByteStream;

public class LogicCompressedString {
    String string = "";
    
    public LogicCompressedString(String stringValue) {
        this.string = stringValue;
    }
    
    public void encode(ByteStream stream) throws Exception {
        final byte[] compressed = ZlibHelper.compress(string);
        
        stream.writeInt(compressed.length + 4);
        stream.writeIntLE(string.length());
        stream.writeBytesWithoutLength(compressed);
    }
    
    public String decode(ByteStream stream) throws Exception {
        //final int length = stream.readInt();
        //final int stringLength = stream.readIntLE();
        final byte[] compressed = stream.readBytes();
        this.string = ZlibHelper.decompress(compressed);
        return string;
    }
}
