package com.banaanae.javasccore.networking;

import com.banaanae.javasccore.titan.ArrayUtils;
import java.util.Arrays;

public class Packet {
    public int id;
    public int len;
    public int version;
    public byte[] bytes;

    public Packet(byte[] data) {
        this.id = ArrayUtils.readUInt16BE(data, 0);
        this.len = (int) ArrayUtils.readUIntBE(data, 2, 3);
        this.version = ArrayUtils.readUInt16BE(data, 5);
        this.bytes = Arrays.copyOfRange(data, 7, len);
    }
}