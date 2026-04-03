package com.banaanae.javasccore.titan;

import java.lang.reflect.Array;

public final class ArrayUtils {
    //  https://stackoverflow.com/a/80503
    public static <T> T concat(T a, T b) {
        if (!a.getClass().isArray() || !b.getClass().isArray()) {
            throw new IllegalArgumentException();
        }

        Class<?> resCompType;
        Class<?> aCompType = a.getClass().getComponentType();
        Class<?> bCompType = b.getClass().getComponentType();

        if (aCompType.isAssignableFrom(bCompType)) {
            resCompType = aCompType;
        } else if (bCompType.isAssignableFrom(aCompType)) {
            resCompType = bCompType;
        } else {
            throw new IllegalArgumentException();
        }

        int aLen = Array.getLength(a);
        int bLen = Array.getLength(b);

        @SuppressWarnings("unchecked")
        T result = (T) Array.newInstance(resCompType, aLen + bLen);
        System.arraycopy(a, 0, result, 0, aLen);
        System.arraycopy(b, 0, result, aLen, bLen);        

        return result;
    }
    
    public static long readUIntBE(byte[] buf, int offset, int length) {
        if (length < 1 || length > 8) throw new IllegalArgumentException("length 1..8");
        if (offset < 0 || offset + length > buf.length) throw new IndexOutOfBoundsException();
        long val = 0;
        for (int i = 0; i < length; i++) {
            val = (val << 8) | (buf[offset + i] & 0xFFL);
        }
        return val;
    }
    
    public static int readUInt16BE(byte[] buf, int offset) {
        if (offset < 0 || offset + 2 > buf.length) throw new IndexOutOfBoundsException();
        return ((buf[offset] & 0xFF) << 8) | (buf[offset + 1] & 0xFF);
    }

    
    public static String toHex(byte[] bytes) {
        char[] hex = new char[bytes.length * 2];
        final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hex[i * 2]     = HEX_CHARS[v >>> 4];
            hex[i * 2 + 1] = HEX_CHARS[v & 0x0F];
        }
        return new String(hex);
    }

}
