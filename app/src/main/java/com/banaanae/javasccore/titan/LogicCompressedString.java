package com.banaanae.javasccore.titan;

import com.banaanae.javasccore.titan.datastream.bytestream.ByteStream;

public class LogicCompressedString {
    byte[] compressed = {};
    int strLen = -1;

    public LogicCompressedString() {
    }

    public LogicCompressedString(String stringValue) {
        this.strLen = stringValue.length();
        this.compressed = ZlibHelper.compress(stringValue);
    }

    public LogicCompressedString(int strLen, byte[] compressed) {
        this.strLen = strLen;
        this.compressed = compressed;
    }

    public void clear() {
        this.strLen = -1;
        this.compressed = new byte[]{};
    }

    public void encode(ByteStream stream) {
        stream.writeInt(compressed.length + 4);
        stream.writeIntLE(strLen);
        stream.writeBytesWithoutLength(compressed);
    }

    public byte[] decode(ByteStream stream) {
        this.compressed = stream.readBytes();
        return this.compressed;
    }

    public byte[] decodeRef(ByteStream stream) {
        this.compressed = stream.readBytes();
        return this.compressed;
    }

    public String getString() {
        return ZlibHelper.decompress(compressed);
    }
}
