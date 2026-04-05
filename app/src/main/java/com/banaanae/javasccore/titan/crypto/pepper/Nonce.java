package com.banaanae.javasccore.titan.crypto.pepper;

import com.banaanae.javasccore.titan.crypto.pepper.PepperEncrypter.SessionKeys;
import java.security.SecureRandom;
import ove.crypto.digest.Blake2b;

public class Nonce {
    byte[] nonce;
    
    public Nonce() {
        SecureRandom secureRandom = new SecureRandom();
        this.nonce = new byte[24];
        secureRandom.nextBytes(this.nonce);
    }
    
    public Nonce(SessionKeys keys) {
        if (keys.server_public_key != null && keys.client_secret_key != null) {
            Blake2b b2b = Blake2b.Digest.newInstance(24);
            if (keys.nonce != null)
                b2b.update(keys.nonce);
            b2b.update(keys.server_public_key);
            b2b.update(keys.client_secret_key);
            nonce = b2b.digest();
        } else if (keys.nonce != null) {
            this.nonce = keys.nonce;
        }
    }
    
    public byte[] bytes() {
        return nonce;
    }
    
    public void increment() {
        int v8 = 2;
        int v10;
        for (var idx = 0; idx < 24; idx++) {
            v10 = v8 + (this.nonce[idx] & 0xFF);
            this.nonce[idx] = (byte) (v10 & 0xFF);
            v8 = v10 / 0x100;
            if (v8 == 0) break;
        }
    }
}
