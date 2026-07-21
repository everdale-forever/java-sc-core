package com.banaanae.javasccore.protocol;

import java.util.HashMap;
import java.util.Map;
import com.banaanae.javasccore.protocol.messages.client.auth.ClientHelloMessage;

public final class LogicMessageFactory {
    private static final Map<Integer, Class<? extends PiranhaMessage>> TYPE_MAP = new HashMap<>();

    private LogicMessageFactory() {}

    public static Class<? extends PiranhaMessage> createMessageByType(int id) {
        return switch (id) {
            case 10100 -> ClientHelloMessage.class;
            default -> null;
        };
    }
}
