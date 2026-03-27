package com.banaanae.javasccore.titan.datastream;

import com.banaanae.javasccore.titan.datastream.bitstream.BitStream;
import com.banaanae.javasccore.titan.datastream.bytestream.ByteStream;
import com.banaanae.javasccore.titan.datastream.bytestream.ByteStreamHelper;

public class DataStream {
    public static ByteStream getByteStream(byte[] bytes) {
        return new ByteStream(bytes);
    }

    public static ByteStreamHelper getByteStreamHelper(ByteStream bytestream) {
        return new ByteStreamHelper(bytestream);
    }

    public static BitStream getBitStream(byte[] bytes) {
        return new BitStream(bytes);
    }
}
