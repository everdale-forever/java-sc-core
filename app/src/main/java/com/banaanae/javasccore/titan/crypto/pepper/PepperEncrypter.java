package com.banaanae.javasccore.titan.crypto.pepper;

import com.banaanae.javasccore.logic.server.LogicConfig;
import com.banaanae.javasccore.titan.ArrayUtils;
import com.banaanae.javasccore.titan.crypto.StreamEncrypter.Encrypter;
import com.iwebpp.crypto.TweetNaclFast;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HexFormat;

public class PepperEncrypter implements Encrypter {
    byte[] client_secret_key;
    byte[] client_public_key;
    byte[] server_public_key;
    byte[] session_token = null;
    Nonce client_nonce;
    Nonce server_nonce;
    TweetNaclFast.Box box;
    
    public class SessionKeys {
        public byte[] server_public_key;
        public byte[] client_secret_key;
        public byte[] nonce = null;
    }
    
    public PepperEncrypter() {
        client_secret_key = HexFormat.of().parseHex(LogicConfig.Crypto.Pepper.CLIENT_SECRET_KEY);
        client_public_key = new byte[32];
        TweetNaclFast.crypto_scalarmult_base(client_public_key, client_secret_key);
        server_public_key = HexFormat.of().parseHex(LogicConfig.Crypto.Pepper.SERVER_PUBLIC_KEY);
        client_nonce = new Nonce();
    }
    
    @Override
    public byte[] encrypt(int type, byte[] bytes) {
        if (type == 20100) {
            this.session_token = Arrays.copyOfRange(bytes, 4, 28);
            return bytes;
        } else if ((type == 20103 || type == 20104) && session_token != null) {
            Nonce nonce = new Nonce(new SessionKeys() {{
                client_secret_key = PepperEncrypter.this.client_secret_key;
                server_public_key = PepperEncrypter.this.server_public_key;
                this.nonce = client_nonce.bytes();
            }});
            this.server_nonce = new Nonce();
            
            SecureRandom secureRandom = new SecureRandom();
            byte[] key = new byte[32];
            secureRandom.nextBytes(key);
            
            byte[] payload = ArrayUtils.concat(server_nonce.bytes(), key);
            payload = ArrayUtils.concat(payload, bytes);
            
            byte[] encrypted = box.box(payload, nonce.bytes());
            this.client_secret_key = key;

            return encrypted;
        } else {
            this.server_nonce.increment();
            return box.box(bytes, server_nonce.bytes());
        }
    }
    
    @Override
    public byte[] decrypt(int type, byte[] bytes) {
        if (type == 10100) {
            return bytes;
        } else if (type == 10101) {
            byte[] encrypted = Arrays.copyOfRange(bytes, 32, bytes.length);
            this.box = new TweetNaclFast.Box(server_public_key, client_secret_key);
            
            Nonce nonce = new Nonce(new SessionKeys() {{
                client_secret_key = PepperEncrypter.this.client_secret_key;
                server_public_key = PepperEncrypter.this.server_public_key;
            }});
            
            byte[] decrypted = box.open(encrypted, nonce.bytes());
            if (decrypted == null) throw new RuntimeException("Decryption failed");
            
            final byte[] session_token = Arrays.copyOfRange(decrypted, 0, 24);
            for (int i = 0; i < 24; i++) {
                if (session_token[i] != this.session_token[i])
                    throw new Error("Invalid session token!");
            }
            
            this.client_nonce = new Nonce(new SessionKeys() {{
                nonce = Arrays.copyOfRange(decrypted, 24, 48);
            }});
            
            return Arrays.copyOfRange(decrypted, 48, decrypted.length);
        } else {
            this.client_nonce.increment();
            
            byte[] decrypted = box.open(bytes, client_nonce.bytes());
            if (decrypted == null) throw new RuntimeException("Decryption failed");

            return decrypted;
        }
    }
}
