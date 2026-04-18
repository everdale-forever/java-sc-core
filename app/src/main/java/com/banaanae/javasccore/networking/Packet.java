package com.banaanae.javasccore.networking;

import com.banaanae.javasccore.titan.ArrayUtils;
import java.util.Arrays;

public class Packet {
    public int id;
    public int len;
    public int version;
    public byte[] bytes;

    public Packet(byte[] data) {
        if (data == null || data.length < 7) {
            this.id = -1;
            this.len = 0;
            this.version = 0;
            this.bytes = new byte[0];
            return;
        }
        
        this.id = ArrayUtils.readUInt16BE(data, 0);
        this.len = (int) ArrayUtils.readUIntBE(data, 2, 3);
        this.version = ArrayUtils.readUInt16BE(data, 5);
        
        if (len < 0 || len > data.length - 7) {
            this.bytes = new byte[0];
            return;
        }
        this.bytes = Arrays.copyOfRange(data, 7, 7 + len);
    }
}