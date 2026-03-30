package com.banaanae.javasccore.protocol;

import com.banaanae.javasccore.networking.Messaging;
import com.banaanae.javasccore.titan.datastream.DataStream;
import java.net.Socket;

public abstract class PiranhaMessage extends Messaging {
    public PiranhaMessage(Socket session) {
        super(session);
        this.session = session;
        this.stream = DataStream.getByteStream(new byte[0]);
    }
    
    @Override
    public void encode() {}
    @Override
    public void decode() {}
    @Override
    public void execute() {}
    @Override
    public abstract int getMessageType();
    @Override
    public int getMessageVersion() {
        return 0;
    };
}
