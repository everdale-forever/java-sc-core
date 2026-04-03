package com.banaanae.javasccore.networking;

import com.banaanae.javasccore.Server.Client;
import com.banaanae.javasccore.protocol.LogicMessageFactory;
import com.banaanae.javasccore.protocol.PiranhaMessage;
import java.lang.reflect.Constructor;

public class MessageHandler {
    Client session;
    
    public MessageHandler(Client session) {
        this.session = session;
    }
    
    public void handle(int id, byte[] bytes) {
        Class<? extends PiranhaMessage> messageClass = LogicMessageFactory.createMessageByType(id);
        if (messageClass == null) {
            session.warn("Unhandled packet: " + id);
            return;
        }

        try {
            Constructor<? extends PiranhaMessage> ctor = null;
            ctor = messageClass.getConstructor(byte[].class, this.session.getClass());

            PiranhaMessage messageInstance;
            messageInstance = ctor.newInstance(bytes, this.session);

            this.session.log("Gotcha " + id + " (" + messageInstance.getClass().getSimpleName() + ") packet!");
            messageInstance.decode();
            messageInstance.execute();
        } catch (ReflectiveOperationException | RuntimeException e) {
            this.session.error(e.toString());
        }
    }
}
