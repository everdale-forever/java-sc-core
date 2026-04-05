package com.banaanae.javasccore.titan.crypto;

import com.banaanae.javasccore.titan.crypto.pepper.PepperEncrypter;
import com.banaanae.javasccore.titan.enums.CryptoTypes;
import com.banaanae.javasccore.titan.Debugger;

class StreamEncrypter {
    Encrypter crypto;
    int cryptoType;
    
    public interface Encrypter {
        byte[] encrypt(int type, byte[] bytes);
        byte[] decrypt(int type, byte[] bytes);
    }
    
    public StreamEncrypter(int type) {
        this.crypto = null;
        this.cryptoType = type;

        switch (type) {
            case 0: // TODO
                // TODO this.crypto = new RC4Encrypter();
                break;
            case 1:
                this.crypto = new PepperEncrypter();
                break;
            default:
                Debugger.warning("Undefined crypto type: " + type);
        }
    }

    public byte[] encrypt(int type, byte[] bytes) {
        switch (this.cryptoType) {
            case 0:
                // return this.crypto.encrypt(bytes);
                return bytes;
            case 1:
                return this.crypto.encrypt(type, bytes);
            default:
                Debugger.warning("Undefined crypto type: " + this.cryptoType);
                return bytes;
        }
    }

    public byte[] decrypt(int type, byte[] bytes) {
        switch (this.cryptoType) {
            case 0:
                // return this.crypto.decrypt(bytes);
                return bytes;
            case 1:
                return this.crypto.decrypt(type, bytes);
            default:
                Debugger.warning("Undefined crypto type: " + this.cryptoType);
                return bytes;
        }
    }
}
