package com.banaanae.javasccore.titan.crypto.rc4;

import com.banaanae.javasccore.logic.server.LogicConfig;
import com.banaanae.javasccore.titan.ArrayUtils;
import com.banaanae.javasccore.titan.crypto.StreamEncrypter.Encrypter;
import java.util.HexFormat;

public class RC4Encrypter implements Encrypter {
    byte[] key;
    byte[] nonce;
    RC4 encryptStream;
    RC4 decryptStream;
    
    public RC4Encrypter() {
        this.key = HexFormat.of().parseHex(LogicConfig.Crypto.RC4.KEY);
        this.nonce = HexFormat.of().parseHex(LogicConfig.Crypto.RC4.NONCE);
        byte[] keyNonce = ArrayUtils.concat(key, nonce);
        this.encryptStream = new RC4(keyNonce);
        encryptStream.update(keyNonce);
        decryptStream.update(keyNonce);
    }
    
    @Override
    public byte[] decrypt(int type, byte[] bytes) {
        return this.decryptStream.update(bytes);
    }

    @Override
    public byte[] encrypt(int type, byte[] bytes) {
        return this.encryptStream.update(bytes);
    }
}
