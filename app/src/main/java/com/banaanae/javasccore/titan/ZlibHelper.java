package com.banaanae.javasccore.titan;

import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZlibHelper {
    public static byte[] compress(String input) throws Exception {
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION, false);
        deflater.setInput(inputBytes);
        deflater.finish();

        byte[] buffer = new byte[1024];
        int len;
        try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
            while (!deflater.finished()) {
                len = deflater.deflate(buffer);
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        } finally {
            deflater.end();
        }
    }

    public static String decompress(byte[] compressed) throws Exception {
        Inflater inflater = new Inflater(false);
        inflater.setInput(compressed);

        byte[] buffer = new byte[1024];
        int len;
        try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
            while (!inflater.finished()) {
                len = inflater.inflate(buffer);
                if (len == 0) {
                    if (inflater.needsInput()) break;
                } else {
                    baos.write(buffer, 0, len);
                }
            }
            return baos.toString(StandardCharsets.UTF_8.name());
        } finally {
            inflater.end();
        }
    }
}
