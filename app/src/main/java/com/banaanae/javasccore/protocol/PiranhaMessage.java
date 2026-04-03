package com.banaanae.javasccore.protocol;

import com.banaanae.javasccore.Server.Client;
import com.banaanae.javasccore.networking.Messaging;
import com.banaanae.javasccore.titan.datastream.DataStream;

public abstract class PiranhaMessage extends Messaging {
    public PiranhaMessage(Client session) {
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
