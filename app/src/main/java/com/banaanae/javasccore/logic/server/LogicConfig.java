package com.banaanae.javasccore.logic.server;

public class LogicConfig {
    public static final int PORT = 9339;
    public static final String SERVER_NAME = "Server";
    
    // Patcher
    
    public static final class Queue {
        public static final int MAX_SIZE = 1024;
        public static final boolean DISCONNECT_ON_OVERFILL = true;
        public static final boolean WARN_ON_OVERFILL = true;
    }
    
    public static final class Session {
        public static final int TIMEOUT_SECONDS = 15;
        public static final int MAX_CONNECTIONS = 100;
        public static final int MAX_CONNECTIONS_PER_IP = 10;
    }

    // Logger

    public static final class Crypto {
        public static final boolean ACTIVATED = false;
        public static final int TYPE = 0;
        
        public static final class RC4 {
            public static final String KEY = "fhsd6f86f67rt8fw78fw789we78r9789wer6re";
            public static final String NONCE = "nonce";
        }
        public static final class Pepper {
            public static final String CLIENT_SECRET_KEY = "";
            public static final String SERVER_PUBLIC_KEY = "";
        }
    }
}
