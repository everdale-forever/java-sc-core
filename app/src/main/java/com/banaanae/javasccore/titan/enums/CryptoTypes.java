package com.banaanae.javasccore.titan.enums;

public enum CryptoTypes {
    RC4(0),
    PEPPER(1);

    private final int code;

    private CryptoTypes(int code) { this.code = code; }

    public int getCode() { return code; }

    public static CryptoTypes fromCode(int code) {
        for (CryptoTypes r : values()) {
            if (r.code == code) return r;
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
