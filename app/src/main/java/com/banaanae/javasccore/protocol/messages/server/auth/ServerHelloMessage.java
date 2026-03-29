package com.banaanae.javasccore.protocol.messages.server.auth;

import com.banaanae.javasccore.protocol.PiranhaMessage;
import com.banaanae.javasccore.titan.datastream.DataStream;
import java.net.Socket;

public class ServerHelloMessage extends PiranhaMessage {
    public ServerHelloMessage(Socket session) {
        super(session);
        this.stream = DataStream.getByteStream(new byte[0]);
    }
    
    @Override
    public void encode() {
        this.stream.writeBytes(new byte[24], 24);
    }
    
    @Override
    public int getMessageType() {
        return 20100;
    }
}
