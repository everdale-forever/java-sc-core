package com.banaanae.javasccore.titan.crypto.rc4;

public class RC4 {

    private final byte[] box = new byte[256];
    private int i = 0;
    private int j = 0;

    public RC4(byte[] key) {
        for (int k = 0; k < 256; k++) {
            box[k] = (byte) k;
        }

        int l = 0;
        for (int k = 0; k < 256; k++) {
            l = (l + (box[k] & 0xFF) + (key[k % key.length] & 0xFF)) & 0xFF;

            byte temp = box[k];
            box[k] = box[l];
            box[l] = temp;
        }
    }

    public byte[] update(byte[] data) {
        for (int k = 0; k < data.length; k++) {
            i = (i + 1) & 0xFF;
            j = (j + (box[i] & 0xFF)) & 0xFF;

            byte temp = box[i];
            box[i] = box[j];
            box[j] = temp;

            int xorIndex = ((box[i] & 0xFF) + (box[j] & 0xFF)) & 0xFF;
            data[k] ^= box[xorIndex];
        }
        return data;
    }

    public void skip(int n) {
        for (int k = 0; k < n; k++) {
            i = (i + 1) & 0xFF;
            j = (j + (box[i] & 0xFF)) & 0xFF;

            byte temp = box[i];
            box[i] = box[j];
            box[j] = temp;
        }
    }
}